package lu.uni.archer.dataflowproblem.intra;

import lu.uni.archer.dataflowproblem.IntraProblem;
import lu.uni.archer.dataflowproblem.intra.impl.FieldPropagation;
import lu.uni.archer.files.LibrariesManager;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Utils;
import soot.*;
import soot.jimple.FieldRef;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.Pair;

import java.util.*;

public class IntraFieldPropagation extends IntraProblem {
    Map<SootMethod, FieldPropagation> results;

    public IntraFieldPropagation() {
        super();
        results = new HashMap<>();
    }

    @Override
    public void solve() {
        List<SootMethod> methodsToSolve = new ArrayList<>();
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!Utils.isSystemClass(sc) && !LibrariesManager.v().isLibrary(sc)) {
                for (SootMethod sm : sc.getMethods()) {
                    if (sm.isConcrete()) {
                        methodsToSolve.add(sm);
                    }
                }
            }
        }
        for (SootMethod sm : methodsToSolve) {
            FieldPropagation fp = new FieldPropagation(new ExceptionalUnitGraph(sm.retrieveActiveBody()));
            results.put(sm, fp);
        }
    }

    @Override
    public Set<FieldRef> getResults(Value v, Unit u) {
        Set<FieldRef> results = new HashSet<>();
        Set<Pair<Value, FieldRef>> resultsComputed = null;
        for (Map.Entry<SootMethod, FieldPropagation> entry : this.results.entrySet()) {
            for (Unit unit : entry.getKey().retrieveActiveBody().getUnits()) {
                if (unit.equals(u)) {
                    resultsComputed = entry.getValue().getFlowAfter(u);
                }
            }
        }
        if (resultsComputed != null) {
            for (Pair<Value, FieldRef> pair : resultsComputed) {
                if (pair.getO1().equals(v)) {
                    results.add(pair.getO2());
                }
            }
        }
        return results;
    }

    @Override
    public String getProblemName() {
        return Constants.INTRA_FIELD_PROPAGATION;
    }
}
