package lu.uni.archer.patch;

import lu.uni.archer.ResultsAccumulator;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.utils.Utils;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallGraphPatcher {
    private final List<Edge> edges;

    private static CallGraphPatcher instance;

    private CallGraphPatcher() {
        this.edges = new ArrayList<>();
    }

    public static CallGraphPatcher v() {
        if (instance == null) {
            instance = new CallGraphPatcher();
        }
        return instance;
    }

    private boolean edgeAlreadyExists(Edge e) {
        Unit srcUnit = e.srcUnit();
        SootMethod callee = e.tgt().method();
        Iterator<Edge> it = Scene.v().getCallGraph().edgesOutOf(srcUnit);
        Edge next;
        while (it.hasNext()) {
            next = it.next();
            if (next.srcUnit().equals(srcUnit) && next.tgt().equals(callee)) {
                return true;
            }
        }
        return false;
    }

    public void patch() {
        List<SootMethod> counted = new ArrayList<>();
        for (Edge e : this.edges) {
            if (MethodsManager.v().isClassAndMethodExecutee(e.tgt().getDeclaringClass(), e.tgt())) {
                ResultsAccumulator.v().incrementNumberOfNewEdge();
                if (!counted.contains(e.tgt())) {
                    ResultsAccumulator.v().addNumberOfExtraStmtCovered(Utils.getNumberOfStmt(e.tgt()));
                    counted.add(e.tgt());
                }
            }
            if (!this.edgeAlreadyExists(e)) {
                Scene.v().getCallGraph().addEdge(e);
            }
        }
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }
}
