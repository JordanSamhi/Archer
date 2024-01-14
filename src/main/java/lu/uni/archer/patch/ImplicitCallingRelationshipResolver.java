package lu.uni.archer.patch;

import lu.uni.archer.analysis.Analyses;
import lu.uni.archer.files.LibrariesManager;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Utils;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import soot.*;
import soot.jimple.ClassConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

import java.util.*;

public class ImplicitCallingRelationshipResolver {

    private final Map<SootMethod, Triplet<Set<SootClass>, Stmt, SootMethod>> executorToPotentialTargets;

    private static ImplicitCallingRelationshipResolver instance;

    public static ImplicitCallingRelationshipResolver v() {
        if (instance == null) {
            instance = new ImplicitCallingRelationshipResolver();
        }
        return instance;
    }

    private ImplicitCallingRelationshipResolver() {
        this.executorToPotentialTargets = new HashMap<>();
    }

    public void instrument() {
        Stmt stmt;
        InvokeExpr ie;
        SootMethod callee;
        List<Quartet<SootMethod, Stmt, Value, SootMethod>> methodsThatNeedClassConstantAnalysis = new ArrayList<>();
        List<Quartet<SootMethod, Stmt, Value, SootMethod>> methodsThatUseCollectionPropagation = new ArrayList<>();
        List<Quartet<SootMethod, Stmt, Value, SootMethod>> methodsThatUsePointsTo = new ArrayList<>();
        List<Triplet<InvokeExpr, Stmt, SootMethod>> infoOfInterest = new ArrayList<>();
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!Utils.isSystemClass(sc) && !LibrariesManager.v().isLibrary(sc)) {
                for (SootMethod sm : sc.getMethods()) {
                    if (sm.isConcrete()) {
                        for (Unit u : sm.retrieveActiveBody().getUnits()) {
                            stmt = (Stmt) u;
                            if (stmt.containsInvokeExpr()) {
                                infoOfInterest.add(new Triplet<>(stmt.getInvokeExpr(), stmt, sm));
                            }
                        }
                    }
                }
            }
        }
        for (Triplet<InvokeExpr, Stmt, SootMethod> triplet : infoOfInterest) {
            InvokeExpr invokeExpr = triplet.getValue0();
            Stmt s = triplet.getValue1();
            SootMethod sm = triplet.getValue2();
            callee = invokeExpr.getMethod();
            if (MethodsManager.v().isExecutor(callee)) {
                Value arg = invokeExpr.getArg(MethodsManager.v().getExecutorArgPosition(callee));
                if (MethodsManager.v().needsClassConstant(callee)) {
                    methodsThatNeedClassConstantAnalysis.add(new Quartet<>(callee, s, arg, sm));
                } else if (MethodsManager.v().needsCollectionPropagation(callee)) {
                    methodsThatUseCollectionPropagation.add(new Quartet<>(callee, s, arg, sm));
                } else {
                    methodsThatUsePointsTo.add(new Quartet<>(callee, s, arg, sm));
                }
            }
        }
        for (Quartet<SootMethod, Stmt, Value, SootMethod> q : methodsThatNeedClassConstantAnalysis) {
            patchMethodsThatNeedClassConstantAnalysis(q.getValue0(), q.getValue1(), q.getValue2(), q.getValue3());
        }
        for (Quartet<SootMethod, Stmt, Value, SootMethod> q : methodsThatUseCollectionPropagation) {
            patchMethodsThatUseCollectionPropagation(q.getValue0(), q.getValue1(), q.getValue2(), q.getValue3());
        }
        for (Quartet<SootMethod, Stmt, Value, SootMethod> q : methodsThatUsePointsTo) {
            patchMethodsThatUsePointsTo(q.getValue0(), q.getValue1(), q.getValue2(), q.getValue3());
        }
        this.patch();
    }

    private void patchMethodsThatUseCollectionPropagation(SootMethod callee, Stmt currentStmt, Value arg, SootMethod currentMethod) {
        Set<SootClass> potentialClassTargets = new HashSet<>();
        Set<Type> potentialTypes = (Set<Type>) Analyses.v().getResults(Constants.POSSIBLE_TYPES, arg, currentStmt);
        for (Type t : potentialTypes) {
            potentialClassTargets.add(Scene.v().getSootClass(t.toString()));
        }
        populatePotentialTargets(callee, potentialClassTargets, currentStmt, currentMethod);
    }

    private void patchMethodsThatUsePointsTo(SootMethod callee, Stmt currentStmt, Value arg, SootMethod currentMethod) {
        RefType rt;
        Set<SootClass> possibleClasses = new HashSet<>();
        Hierarchy hierarchy = Scene.v().getActiveHierarchy();
        if (arg instanceof Local) {
            Set<Type> possibleTypes = new HashSet<>();
            possibleTypes.addAll(Scene.v().getPointsToAnalysis().reachingObjects((Local) arg).possibleTypes());
            possibleTypes.addAll((Set<Type>) Analyses.v().getResults(Constants.POSSIBLE_TYPES, arg, currentStmt));
            for (Type t : possibleTypes) {
                if (t instanceof AnySubType) {
                    rt = ((AnySubType) t).getBase();
                    SootClass rtClass = rt.getSootClass();
                    if (rtClass.isInterface()) {
                        possibleClasses.addAll(hierarchy.getImplementersOf(rtClass));
                    } else {
                        possibleClasses.addAll(hierarchy.getSubclassesOfIncluding(rt.getSootClass()));
                    }
                } else if (t instanceof RefType) {
                    rt = (RefType) t;
                    possibleClasses.add(rt.getSootClass());
                }
            }
            populatePotentialTargets(callee, possibleClasses, currentStmt, currentMethod);
        }
    }

    private void patchMethodsThatNeedClassConstantAnalysis(SootMethod callee, Stmt currentStmt, Value arg, SootMethod currentMethod) {
        Set<SootClass> potentialClassTargets = new HashSet<>();
        Set<ClassConstant> classConstants = (Set<ClassConstant>) Analyses.v().getResults(Constants.CLASS_CONSTANT_PROPAGATION, arg, currentStmt);
        for (ClassConstant cc : classConstants) {
            potentialClassTargets.add(Scene.v().getSootClass(Utils.javaSigToSootSig(cc.value)));
        }
        populatePotentialTargets(callee, potentialClassTargets, currentStmt, currentMethod);
    }

    private void patch() {
        for (Map.Entry<SootMethod, Triplet<Set<SootClass>, Stmt, SootMethod>> entry : this.executorToPotentialTargets.entrySet()) {
            SootMethod callee = entry.getKey();
            Triplet<Set<SootClass>, Stmt, SootMethod> triplet = entry.getValue();
            Set<SootClass> potentialClassTargets = triplet.getValue0();
            Stmt currentStmt = triplet.getValue1();
            SootMethod currentMethod = triplet.getValue2();
            Set<SootClass> potentialClassTargetsWithTargetMethod = new HashSet<>();
            for (SootClass clazz : potentialClassTargets) {
                SootMethod potentialTgt = clazz.getMethodUnsafe(MethodsManager.v().getExecutee(callee).getSubSignature());
                if (potentialTgt != null) {
                    potentialClassTargetsWithTargetMethod.add(clazz);
                } else {
                    List<SootClass> parents = Utils.getAllSuperClasses(clazz);
                    for (SootClass parent : parents) {
                        potentialTgt = parent.getMethodUnsafe(MethodsManager.v().getExecutee(callee).getSubSignature());
                        if (potentialTgt != null) {
                            potentialClassTargetsWithTargetMethod.add(parent);
                        }
                    }
                }
            }
            MethodBuilder.v().buildMethod(potentialClassTargetsWithTargetMethod, callee, currentStmt, currentMethod);
        }
    }

    private void populatePotentialTargets(SootMethod callee, Set<SootClass> potentialClassTargets, Stmt currentStmt, SootMethod currentMethod) {
        Triplet<Set<SootClass>, Stmt, SootMethod> triplet = this.executorToPotentialTargets.get(callee);
        if (!potentialClassTargets.isEmpty()) {
            if (triplet == null) {
                Set<SootClass> potentialTargets = new HashSet<>(potentialClassTargets);
                triplet = new Triplet<>(potentialTargets, currentStmt, currentMethod);
                this.executorToPotentialTargets.put(callee, triplet);
            }
            Set<SootClass> potentialTargets = triplet.getValue0();
            potentialTargets.addAll(potentialClassTargets);
        }
    }

    public Map<SootMethod, Triplet<Set<SootClass>, Stmt, SootMethod>> getExecutorToPotentialTargets() {
        return executorToPotentialTargets;
    }
}