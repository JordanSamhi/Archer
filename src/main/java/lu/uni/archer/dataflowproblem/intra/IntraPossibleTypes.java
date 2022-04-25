package lu.uni.archer.dataflowproblem.intra;

import lu.uni.archer.dataflowproblem.IntraProblem;
import lu.uni.archer.dataflowproblem.intra.impl.PossibleTypesPropagation;
import lu.uni.archer.files.LibrariesManager;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Utils;
import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.Pair;

import java.util.*;

public class IntraPossibleTypes extends IntraProblem {
    Map<SootMethod, PossibleTypesPropagation> results;

    public IntraPossibleTypes() {
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
            PossibleTypesPropagation pt = new PossibleTypesPropagation(new ExceptionalUnitGraph(sm.retrieveActiveBody()));
            results.put(sm, pt);
        }
    }

    @Override
    public Set<Type> getResults(Value v, Unit u) {
        Set<Type> results = new HashSet<>();
        Set<Pair<Value, Type>> resultsComputed = null;
        for (Map.Entry<SootMethod, PossibleTypesPropagation> entry : this.results.entrySet()) {
            for (Unit unit : entry.getKey().retrieveActiveBody().getUnits()) {
                if (unit.equals(u)) {
                    resultsComputed = entry.getValue().getFlowAfter(u);
                }
            }
        }
        if (resultsComputed != null) {
            for (Pair<Value, Type> pair : resultsComputed) {
                if (pair.getO1().equals(v)) {
                    results.add(pair.getO2());
                }
            }
        }
        return results;
    }

    @Override
    public String getProblemName() {
        return Constants.INTRA_POSSIBLE_TYPES;
    }
}
