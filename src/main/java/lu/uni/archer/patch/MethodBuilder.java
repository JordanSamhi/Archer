package lu.uni.archer.patch;

import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Utils;
import soot.*;
import soot.jimple.*;
import soot.jimple.infoflow.cfg.FlowDroidEssentialMethodTag;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.*;

public class MethodBuilder {
    private static MethodBuilder instance;
    List<InvokeExpr> invokeExprs;
    private final Map<SootMethod, SootMethod> oldToNewMethod;

    private MethodBuilder() {
        this.oldToNewMethod = new HashMap<>();
        this.invokeExprs = new ArrayList<>();
        this.populateInvokeExprs();
    }

    private void populateInvokeExprs() {
        for (SootClass sootClass : Scene.v().getClasses()) {
            List<SootMethod> methods = sootClass.getMethods();
            for (SootMethod sm : methods) {
                if (sm.isConcrete() && sm.getDeclaringClass().resolvingLevel() == SootClass.BODIES && sm.hasActiveBody()) {
                    for (Unit u : sm.retrieveActiveBody().getUnits()) {
                        Stmt stmt = (Stmt) u;
                        if (stmt.containsInvokeExpr()) {
                            InvokeExpr ie = stmt.getInvokeExpr();
                            invokeExprs.add(ie);
                        }
                    }
                }
            }
        }
    }

    public static MethodBuilder v() {
        if (instance == null) {
            instance = new MethodBuilder();
        }
        return instance;
    }

    public void buildMethod(Set<SootClass> potentialClassTargets, SootMethod methodToInstrument, Stmt currentStmt, SootMethod currentMethod) {
        SootClass methodClass = methodToInstrument.getDeclaringClass();
        methodClass.setLibraryClass();
        List<Type> paramTypes = methodToInstrument.getParameterTypes();
        SootMethod newMethod;
        int modifiers = methodToInstrument.getModifiers();
        if (methodToInstrument.isAbstract()) {
            modifiers -= Modifier.ABSTRACT;
            newMethod = new SootMethod(methodToInstrument.getName(), methodToInstrument.getParameterTypes(), methodToInstrument.getReturnType(), modifiers);
        } else {
            methodToInstrument.releaseActiveBody();
            newMethod = methodToInstrument;
        }

        JimpleBody b = Jimple.v().newBody(newMethod);
        newMethod.setActiveBody(b);
        newMethod.addTag(new FlowDroidEssentialMethodTag());
        newMethod.setPhantom(false);

        if (!newMethod.isStatic()) {
            Local thisLocal = Jimple.v().newLocal("this", methodClass.getType());
            b.getLocals().add(thisLocal);
            b.getUnits().add(Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(methodClass.getType())));
        }
        int paramPosition = 0;
        for (Type t : paramTypes) {
            Local param = Jimple.v().newLocal("param" + Utils.getNextIdx(), t);
            b.getLocals().add(param);
            b.getUnits().add(Jimple.v().newIdentityStmt(param, Jimple.v().newParameterRef(t, paramPosition)));
            paramPosition++;
        }

        for (SootClass clazz : potentialClassTargets) {
            Local loc = Jimple.v().newLocal("target" + Utils.getNextIdx(), clazz.getType());
            b.getLocals().add(loc);
            if (clazz.getMethodUnsafe(Constants.INIT) == null) {
                this.generateConstructor(clazz);
            }
            b.getUnits().add(
                    Jimple.v().newAssignStmt(loc, Jimple.v().newNewExpr(clazz.getType())));
            b.getUnits().add(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(loc,
                    Utils.getMethodRef(clazz.getName(), Constants.INIT))));
            SootMethod targetMethod = clazz.getMethodUnsafe(MethodsManager.v().getExecutee(methodToInstrument).getSubSignature());
            List<Value> params = new ArrayList<>();
            for (Type _ : targetMethod.getParameterTypes()) {
                params.add(NullConstant.v());
            }
            Stmt callStmt = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(loc,
                    Utils.getMethodRef(clazz.getName(), targetMethod.getSubSignature()), params));
            b.getUnits().add(callStmt);
            CallGraphPatcher.v().addEdge(new Edge(newMethod, callStmt, targetMethod));
        }
        CallGraphPatcher.v().addEdge(new Edge(currentMethod, currentStmt, newMethod));

        if (newMethod.getReturnType().equals(VoidType.v())) {
            b.getUnits().add(Jimple.v().newReturnVoidStmt());
        } else {
            b.getUnits().add(Jimple.v().newReturnStmt(NullConstant.v()));
        }
        b.validate();
        List<InvokeExpr> invokeExprsToModify = new ArrayList<>();
        for (InvokeExpr ie : this.invokeExprs) {
            if (ie.getMethod().equals(methodToInstrument)) {
                invokeExprsToModify.add(ie);
            }
        }
        this.oldToNewMethod.put(methodToInstrument, newMethod);
        methodClass.removeMethod(methodToInstrument);
        methodClass.addMethod(newMethod);
        for (InvokeExpr ie : invokeExprsToModify) {
            ie.setMethodRef(newMethod.makeRef());
            this.invokeExprs.remove(ie);
        }
        InvokeExpr ie = currentStmt.getInvokeExpr();
        ie.setMethodRef(newMethod.makeRef());
    }

    private void generateConstructor(SootClass clazz) {
        clazz.setApplicationClass();

        SootMethod classConstructor = new SootMethod("<init>", new ArrayList<>(), VoidType.v());
        classConstructor.setPhantom(false);
        classConstructor.addTag(new FlowDroidEssentialMethodTag());

        Body b = Jimple.v().newBody(classConstructor);
        classConstructor.setActiveBody(b);

        Local thisLocal = Jimple.v().newLocal("this", clazz.getType());
        b.getLocals().add(thisLocal);
        b.getUnits().add(Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(clazz.getType())));

        b.getUnits().add(Jimple.v().newReturnVoidStmt());
        b.validate();
        clazz.addMethod(classConstructor);
    }

    public Map<SootMethod, SootMethod> getOldToNewMethod() {
        return oldToNewMethod;
    }
}
