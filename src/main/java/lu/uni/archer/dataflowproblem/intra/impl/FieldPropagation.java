package lu.uni.archer.dataflowproblem.intra.impl;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.Pair;

import java.util.HashSet;
import java.util.Set;

public class FieldPropagation extends ForwardFlowAnalysis<Unit, Set<Pair<Value, FieldRef>>> {

    public FieldPropagation(DirectedGraph<Unit> graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected void flowThrough(Set<Pair<Value, FieldRef>> in, Unit d, Set<Pair<Value, FieldRef>> out) {
        Set<Pair<Value, FieldRef>> gen = new HashSet<>(),
                kill = new HashSet<>();

        if (d instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt) d;
            final Value right = ds.getRightOp();
            final Value left = ds.getLeftOp();
            if (left instanceof Local) {
                final Local leftLocal = (Local) left;
                if (right instanceof FieldRef) {
                    FieldRef fr = (FieldRef) right;
                    gen.add(new Pair<>(left, fr));
                    for (Pair<Value, FieldRef> pair : in) {
                        if (pair.getO1().equals(left)) {
                            kill.add(pair);
                        }
                    }
                } else if (right instanceof Local) {
                    for (Pair<Value, FieldRef> pair : in) {
                        if (pair.getO1().equals(right)) {
                            gen.add(new Pair<>(left, pair.getO2()));
                        }
                    }
                    for (Pair<Value, FieldRef> pair : in) {
                        if (pair.getO1().equals(left)) {
                            kill.add(pair);
                        }
                    }
                }
            }
        }
        copy(in, out);
        out.removeAll(kill);
        out.addAll(gen);
    }

    @Override
    protected Set<Pair<Value, FieldRef>> newInitialFlow() {
        return new HashSet<>();
    }

    @Override
    protected void merge(Set<Pair<Value, FieldRef>> in1, Set<Pair<Value, FieldRef>> in2, Set<Pair<Value, FieldRef>> out) {
        out.addAll(in1);
        out.addAll(in2);
    }

    @Override
    protected void copy(Set<Pair<Value, FieldRef>> source, Set<Pair<Value, FieldRef>> dest) {
        dest.addAll(source);
    }
}