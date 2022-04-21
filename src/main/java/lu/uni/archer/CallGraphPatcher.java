package lu.uni.archer;

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
        for (Edge e : this.edges) {
            if (!this.edgeAlreadyExists(e)) {
                Scene.v().getCallGraph().addEdge(e);
            }
        }
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }
}
