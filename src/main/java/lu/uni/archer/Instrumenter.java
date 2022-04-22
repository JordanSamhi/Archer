package lu.uni.archer;

import lu.uni.archer.analysis.Analyses;
import lu.uni.archer.files.LibrariesManager;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.MethodBuilder;
import lu.uni.archer.utils.Utils;
import org.javatuples.Triplet;
import soot.*;
import soot.jimple.ClassConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.toolkits.scalar.Pair;

import java.util.*;

public class Instrumenter {

    private final CallGraph cg;
    private final Map<SootMethod, Triplet<List<SootClass>, Stmt, SootMethod>> executorToPotentialTargets;

    private static Instrumenter instance;

    public static Instrumenter v() {
        if (instance == null) {
            instance = new Instrumenter();
        }
        return instance;
    }

    private Instrumenter() {
        this.cg = Scene.v().getCallGraph();
        this.executorToPotentialTargets = new HashMap<>();
    }

    public void instrument() {
        Stmt stmt;
        InvokeExpr ie;
        SootMethod callee;
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
                                        patchMethodsThatNeedClassConstantAnalysis(callee, stmt, arg, sm);
                                    } else if (MethodsManager.v().needsCollectionPropagation(callee)) {
                                        patchMethodsThatUseCollectionPropagation(callee, stmt, arg, sm);
                                    } else {
                                        patchMethodsThatUsePointsTo(callee, stmt, arg, sm);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.patch();
    }

    private void patchMethodsThatUseCollectionPropagation(SootMethod callee, Stmt currentStmt, Value arg, SootMethod currentMethod) {
        List<SootClass> potentialClassTargets = new ArrayList<>();
        for (Object o : Analyses.v().getSolver(Constants.POSSIBLE_TYPES).ifdsResultsAt(currentStmt)) {
            Pair<Value, Type> pair = (Pair<Value, Type>) o;
            if (arg.equals(pair.getO1())) {
                potentialClassTargets.add(Scene.v().getSootClass(pair.getO2().toString()));
            }
        }
        populatePotentialTargets(callee, potentialClassTargets, currentStmt, currentMethod);
    }

    private void patchMethodsThatUsePointsTo(SootMethod callee, Stmt currentStmt, Value arg, SootMethod currentMethod) {
        RefType rt;
        List<SootClass> possibleClasses = new ArrayList<>();
        if (arg instanceof Local) {
            Set<Type> possibleTypes = Scene.v().getPointsToAnalysis().reachingObjects((Local) arg).possibleTypes();
            for (Type t : possibleTypes) {
                if (t instanceof AnySubType) {
                    rt = ((AnySubType) t).getBase();
                    possibleClasses.addAll(Scene.v().getActiveHierarchy().getSubclassesOfIncluding(rt.getSootClass()));
                } else if (t instanceof RefType) {
                    rt = (RefType) t;
                    possibleClasses.add(rt.getSootClass());
                }
            }
            populatePotentialTargets(callee, possibleClasses, currentStmt, currentMethod);
        }
    }

    private void patchMethodsThatNeedClassConstantAnalysis(SootMethod callee, Stmt currentStmt, Value arg, SootMethod currentMethod) {
        List<SootClass> potentialClassTargets = new ArrayList<>();
        for (Object o : Analyses.v().getSolver(Constants.CLASS_CONSTANT_PROPAGATION).ifdsResultsAt(currentStmt)) {
            Pair<Local, ClassConstant> pair = (Pair<Local, ClassConstant>) o;
            if (arg.equals(pair.getO1())) {
                potentialClassTargets.add(Scene.v().getSootClass(Utils.javaSigToSootSig(pair.getO2().value)));
            }
        }
        populatePotentialTargets(callee, potentialClassTargets, currentStmt, currentMethod);
    }

    private void patch() {
        for (Map.Entry<SootMethod, Triplet<List<SootClass>, Stmt, SootMethod>> entry : this.executorToPotentialTargets.entrySet()) {
            SootMethod callee = entry.getKey();
            Triplet<List<SootClass>, Stmt, SootMethod> triplet = entry.getValue();
            List<SootClass> potentialClassTargets = triplet.getValue0();
            Stmt currentStmt = triplet.getValue1();
            SootMethod currentMethod = triplet.getValue2();
            List<SootClass> potentialClassTargetsWithTargetMethod = new ArrayList<>();
            for (SootClass clazz : potentialClassTargets) {
                SootMethod potentialTgt = clazz.getMethodUnsafe(MethodsManager.v().getExecutee(callee).getSubSignature());
                if (potentialTgt != null) {
                    potentialClassTargetsWithTargetMethod.add(clazz);
                }
            }
            MethodBuilder.v().buildMethod(potentialClassTargetsWithTargetMethod, callee, currentStmt, currentMethod);
        }
    }

    private void populatePotentialTargets(SootMethod callee, List<SootClass> potentialClassTargets, Stmt currentStmt, SootMethod currentMethod) {
        Triplet<List<SootClass>, Stmt, SootMethod> triplet = this.executorToPotentialTargets.get(callee);
        if (triplet == null) {
            triplet = new Triplet<>(new ArrayList<>(), currentStmt, currentMethod);
            this.executorToPotentialTargets.put(callee, triplet);
        }
        List<SootClass> potentialTargets = triplet.getValue0();
        potentialTargets.addAll(potentialClassTargets);
    }
}