package lu.uni.archer;

import lu.uni.archer.analysis.Analyses;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.files.MethodsRemoveCallGraphManager;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Utils;
import soot.*;
import soot.jimple.ClassConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.scalar.Pair;

import java.util.*;

public class CallGraphPatcher {

    private final CallGraph cg;

    private static CallGraphPatcher instance;

    public static CallGraphPatcher v() {
        if (instance == null) {
            instance = new CallGraphPatcher();
        }
        return instance;
    }

    private CallGraphPatcher() {
        this.cg = Scene.v().getCallGraph();
    }

    public void updateCallGraph() {
        this.removeLinks();
        this.addLinks();
    }

    private void addLinks() {
        Stmt stmt;
        InvokeExpr ie;
        SootMethod callee;
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!Utils.isSystemClass(sc)) {
                for (SootMethod sm : sc.getMethods()) {
                    if (sm.hasActiveBody()) {
                        for (Unit u : sm.retrieveActiveBody().getUnits()) {
                            stmt = (Stmt) u;
                            if (stmt.containsInvokeExpr()) {
                                ie = stmt.getInvokeExpr();
                                callee = ie.getMethod();
                                if (MethodsManager.v().isExecutor(callee)) {
                                    patchCallGraphForMethodsThatNeedClassConstantAnalysis(callee, stmt, ie);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void patchCallGraphForMethodsThatNeedClassConstantAnalysis(SootMethod callee, Stmt stmt, InvokeExpr ie) {
        int paramPosition = MethodsManager.v().getExecutorArgPosition(callee);
        if (paramPosition >= 0) {
            Value arg = ie.getArg(paramPosition);
            if (MethodsManager.v().needsClassConstant(callee)) {
                Set<SootClass> classes = this.getClassConstantDalaFlowValues(stmt, arg);
                for (SootClass clazz : classes) {
                    SootMethod potentialTgt = clazz.getMethodUnsafe(MethodsManager.v().getExecutee(callee).getSubSignature());
                    if (potentialTgt != null) {
                        Edge e = new Edge(callee, stmt, potentialTgt);
                        Scene.v().getCallGraph().addEdge(e);
                    }
                }
            }
        }
    }

    private Set<SootClass> getClassConstantDalaFlowValues(Stmt stmt, Value arg) {
        Set<SootClass> cc = new HashSet<>();
        for (Object o : Analyses.v().getSolver(Constants.CLASS_CONSTANT_PROPAGATION).ifdsResultsAt(stmt)) {
            Pair<Local, ClassConstant> pair = (Pair<Local, ClassConstant>) o;
            if (arg.equals(pair.getO1())) {
                cc.add(Scene.v().getSootClass(Utils.javaSigToSootSig(pair.getO2().value)));
            }
        }
        return cc;
    }

    private void removeLinks() {
        List<Edge> edgesToRemove = new ArrayList<>();
        Iterator<Edge> it = this.cg.iterator();
        Edge next;
        SootMethod tgt;
        while (it.hasNext()) {
            next = it.next();
            tgt = next.tgt();
            if (tgt != null) {
                if (MethodsRemoveCallGraphManager.v().isInMethodsToRemove(tgt)) {
                    edgesToRemove.add(next);
                }

            }
        }
        for (Edge e : edgesToRemove) {
            this.cg.removeEdge(e);
        }
    }
}