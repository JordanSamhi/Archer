package lu.uni.archer;

import lu.uni.archer.analysis.Analyses;
import lu.uni.archer.files.LibrariesManager;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.MethodBuilder;
import lu.uni.archer.utils.Utils;
import org.javatuples.Quartet;
import soot.*;
import soot.jimple.ClassConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.toolkits.scalar.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Instrumenter {

    private final CallGraph cg;

    private static Instrumenter instance;

    public static Instrumenter v() {
        if (instance == null) {
            instance = new Instrumenter();
        }
        return instance;
    }

    private Instrumenter() {
        this.cg = Scene.v().getCallGraph();
    }

    public void instrument() {
        Stmt stmt;
        InvokeExpr ie;
        SootMethod callee;
        List<Quartet<SootMethod, Stmt, Value, SootMethod>> elementsForPatchingMethodsThatNeedClassConstantAnalysis,
                elementsForMethodsThatUsePointsTo, elementsForPatchingMethodsThatNeedCollectionMethodPropagation;
        elementsForPatchingMethodsThatNeedClassConstantAnalysis = new ArrayList<>();
        elementsForMethodsThatUsePointsTo = new ArrayList<>();
        elementsForPatchingMethodsThatNeedCollectionMethodPropagation = new ArrayList<>();
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!Utils.isSystemClass(sc) && !LibrariesManager.v().isLibrary(sc)) {
                for (SootMethod sm : sc.getMethods()) {
                    if (sm.hasActiveBody()) {
                        for (Unit u : sm.retrieveActiveBody().getUnits()) {
                            stmt = (Stmt) u;
                            if (stmt.containsInvokeExpr()) {
                                ie = stmt.getInvokeExpr();
                                callee = ie.getMethod();
                                if (MethodsManager.v().isExecutor(callee)) {
                                    Value arg = ie.getArg(MethodsManager.v().getExecutorArgPosition(callee));
                                    if (MethodsManager.v().needsClassConstant(callee)) {
                                        elementsForPatchingMethodsThatNeedClassConstantAnalysis.add(new Quartet<>(callee, stmt, arg, sm));
                                        if (MethodsManager.v().needsCollectionPropagation(callee)) {
                                            //TODO
                                        }
                                    } else if (MethodsManager.v().needsCollectionPropagation(callee)) {
                                        elementsForPatchingMethodsThatNeedCollectionMethodPropagation.add(new Quartet<>(callee, stmt, arg, sm));
                                    } else {
                                        elementsForMethodsThatUsePointsTo.add(new Quartet<>(callee, stmt, arg, sm));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Quartet<SootMethod, Stmt, Value, SootMethod> quartet : elementsForPatchingMethodsThatNeedClassConstantAnalysis) {
            patchMethodsThatNeedClassConstantAnalysis(quartet.getValue0(), quartet.getValue1(), quartet.getValue2(), quartet.getValue3());
        }
        for (Quartet<SootMethod, Stmt, Value, SootMethod> quartet : elementsForMethodsThatUsePointsTo) {
            patchMethodsThatUsePointsTo(quartet.getValue0(), quartet.getValue1(), quartet.getValue2(), quartet.getValue3());
        }
        for (Quartet<SootMethod, Stmt, Value, SootMethod> quartet : elementsForPatchingMethodsThatNeedCollectionMethodPropagation) {
            patchMethodsThatUseCollectionPropagation(quartet.getValue0(), quartet.getValue1(), quartet.getValue2(), quartet.getValue3());
        }
    }

    private void patchMethodsThatUseCollectionPropagation(SootMethod callee, Stmt stmt, Value arg, SootMethod currentMethod) {
        List<SootClass> potentialClassTargets = new ArrayList<>();
        for (Object o : Analyses.v().getSolver(Constants.POSSIBLE_TYPES).ifdsResultsAt(stmt)) {
            Pair<Value, Type> pair = (Pair<Value, Type>) o;
            if (arg.equals(pair.getO1())) {
                potentialClassTargets.add(Scene.v().getSootClass(pair.getO2().toString()));
            }
        }
        this.patch(potentialClassTargets, callee, stmt, currentMethod);
    }

    private void patchMethodsThatUsePointsTo(SootMethod callee, Stmt currentStmt, Value arg, SootMethod currentMethod) {
        if (arg instanceof Local) {
            Set<Type> possibleTypes = Scene.v().getPointsToAnalysis().reachingObjects((Local) arg).possibleTypes();
            for (Type t : possibleTypes) {
                if (t instanceof AnySubType) {
                    RefType rt = ((AnySubType) t).getBase();
                    SootClass base = rt.getSootClass();
                    List<SootClass> possibleClasses = Scene.v().getActiveHierarchy().getSubclassesOfIncluding(base);
                    this.patch(possibleClasses, callee, currentStmt, currentMethod);
                }
            }
        }
    }

    private void patch(List<SootClass> potentialClassTargets, SootMethod callee, Stmt currentStmt, SootMethod currentMethod) {
        List<SootClass> potentialClassTargetsWithTargetMethod = new ArrayList<>();
        for (SootClass clazz : potentialClassTargets) {
            SootMethod potentialTgt = clazz.getMethodUnsafe(MethodsManager.v().getExecutee(callee).getSubSignature());
            if (potentialTgt != null) {
                potentialClassTargetsWithTargetMethod.add(clazz);
            }
        }
        MethodBuilder.v().buildMethod(potentialClassTargetsWithTargetMethod, callee, currentStmt, currentMethod);
    }

    private void patchMethodsThatNeedClassConstantAnalysis(SootMethod callee, Stmt stmt, Value arg, SootMethod currentMethod) {
        List<SootClass> potentialClassTargets = new ArrayList<>();
        for (Object o : Analyses.v().getSolver(Constants.CLASS_CONSTANT_PROPAGATION).ifdsResultsAt(stmt)) {
            Pair<Local, ClassConstant> pair = (Pair<Local, ClassConstant>) o;
            if (arg.equals(pair.getO1())) {
                potentialClassTargets.add(Scene.v().getSootClass(Utils.javaSigToSootSig(pair.getO2().value)));
            }
        }
        this.patch(potentialClassTargets, callee, stmt, currentMethod);
    }
}