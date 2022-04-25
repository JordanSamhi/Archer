package lu.uni.archer.dataflowproblem.intra.impl;

import soot.Local;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is based on: https://github.com/soot-oss/soot/blob/develop/src/main/java/soot/jimple/toolkits/ide/exampleproblems/IFDSPossibleTypes.java
 */
public class PossibleTypesPropagation extends ForwardFlowAnalysis<Unit, Set<Pair<Value, Type>>> {

    public PossibleTypesPropagation(DirectedGraph<Unit> graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected void flowThrough(Set<Pair<Value, Type>> in, Unit d, Set<Pair<Value, Type>> out) {
        Set<Pair<Value, Type>> gen = new HashSet<>(),
                kill = new HashSet<>();

        if (d instanceof DefinitionStmt) {
            DefinitionStmt def = (DefinitionStmt) d;
            Value left = def.getLeftOp();
            Value right = def.getRightOp();
            if (left instanceof Local && right instanceof NewExpr) {
                NewExpr ne = (NewExpr) right;
                gen.add(new Pair<>(left, ne.getType()));
                for (Pair<Value, Type> pair : in) {
                    if (pair.getO1().equals(left)) {
                        kill.add(pair);
                    }
                }
            }
        }
        copy(in, out);
        out.removeAll(kill);
        out.addAll(gen);
    }

    @Override
    protected Set<Pair<Value, Type>> newInitialFlow() {
        return new HashSet<>();
    }

    @Override
    protected void merge(Set<Pair<Value, Type>> in1, Set<Pair<Value, Type>> in2, Set<Pair<Value, Type>> out) {
        out.addAll(in1);
        out.addAll(in2);
    }

    @Override
    protected void copy(Set<Pair<Value, Type>> source, Set<Pair<Value, Type>> dest) {
        dest.addAll(source);
    }
}
