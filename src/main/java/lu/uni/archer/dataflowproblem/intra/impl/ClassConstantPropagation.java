package lu.uni.archer.dataflowproblem.intra.impl;

import lu.uni.archer.files.ClassConstantMethodsManager;
import lu.uni.archer.files.CollectionMethodsToPropagateTypeManager;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.*;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.Pair;

import java.util.HashSet;
import java.util.Set;

public class ClassConstantPropagation extends ForwardFlowAnalysis<Unit, Set<Pair<Value, ClassConstant>>> {

    public ClassConstantPropagation(DirectedGraph<Unit> graph) {
        super(graph);
        doAnalysis();
    }

    @Override
    protected void flowThrough(Set<Pair<Value, ClassConstant>> in, Unit d, Set<Pair<Value, ClassConstant>> out) {
        Set<Pair<Value, ClassConstant>> gen = new HashSet<>(),
                kill = new HashSet<>();

        if (d instanceof DefinitionStmt) {
            DefinitionStmt def = (DefinitionStmt) d;
            Value left = def.getLeftOp();
            Value right = def.getRightOp();
            if (def.containsInvokeExpr()) {
                InvokeExpr ie = def.getInvokeExpr();
                SootMethod callee = ie.getMethod();
                if (ClassConstantMethodsManager.v().isInMethodsThatPropagateBaseToReceiver(callee)) {
                    if (ie instanceof InstanceInvokeExpr) {
                        InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                        Value base = iie.getBase();
                        for (Pair<Value, ClassConstant> pair : in) {
                            if (pair.getO1().equals(base)) {
                                gen.add(new Pair<>(left, pair.getO2()));
                            }
                        }
                        for (Pair<Value, ClassConstant> pair : in) {
                            if (pair.getO1().equals(left)) {
                                kill.add(pair);
                            }
                        }
                    }
                }
            } else {
                if (right instanceof ClassConstant) {
                    gen.add(new Pair<>(left, (ClassConstant) right));
                    for (Pair<Value, ClassConstant> pair : in) {
                        if (pair.getO1().equals(left)) {
                            kill.add(pair);
                        }
                    }
                } else if (right instanceof Ref || right instanceof Local) {
                    for (Pair<Value, ClassConstant> pair : in) {
                        if (pair.getO1().equals(right)) {
                            gen.add(new Pair<>(left, pair.getO2()));
                        }
                    }
                    for (Pair<Value, ClassConstant> pair : in) {
                        if (pair.getO1().equals(left)) {
                            kill.add(pair);
                        }
                    }
                } else if (right instanceof CastExpr) {
                    CastExpr ce = (CastExpr) right;
                    Value v = ce.getOp();
                    for (Pair<Value, ClassConstant> pair : in) {
                        if (pair.getO1().equals(v)) {
                            gen.add(new Pair<>(left, pair.getO2()));
                        }
                    }
                    for (Pair<Value, ClassConstant> pair : in) {
                        if (pair.getO1().equals(left)) {
                            kill.add(pair);
                        }
                    }
                }
            }
        } else if (d instanceof InvokeStmt) {
            InvokeStmt is = (InvokeStmt) d;
            InvokeExpr ie = is.getInvokeExpr();
            SootMethod callee = ie.getMethod();
            if (ie instanceof InstanceInvokeExpr) {
                InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                Value base = iie.getBase();
                if (ClassConstantMethodsManager.v().isInMethodsThatGenerateToBase(callee)) {
                    Value classConstant = ClassConstantMethodsManager.v().getClassConstant(ie);
                    if (classConstant != null) {
                        if (classConstant instanceof ClassConstant) {
                            gen.add(new Pair<>(base, (ClassConstant) classConstant));
                        } else if (classConstant instanceof Ref || classConstant instanceof Local) {
                            for (Pair<Value, ClassConstant> pair : in) {
                                if (pair.getO1().equals(classConstant)) {
                                    gen.add(new Pair<>(base, pair.getO2()));
                                }
                            }
                            for (Pair<Value, ClassConstant> pair : in) {
                                if (pair.getO1().equals(base)) {
                                    kill.add(pair);
                                }
                            }
                        }
                    }
                } else if (CollectionMethodsToPropagateTypeManager.v().isCollectionMethodToPropagateType(callee)) {
                    int argPosition = CollectionMethodsToPropagateTypeManager.v().getArgPosition(callee);
                    if (argPosition != -1) {
                        Value arg = iie.getArg(argPosition);
                        for (Pair<Value, ClassConstant> pair : in) {
                            if (pair.getO1().equals(arg)) {
                                gen.add(new Pair<>(base, pair.getO2()));
                            }
                        }
                        for (Pair<Value, ClassConstant> pair : in) {
                            if (pair.getO1().equals(base)) {
                                kill.add(pair);
                            }
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
    protected Set<Pair<Value, ClassConstant>> newInitialFlow() {
        return new HashSet<>();
    }

    @Override
    protected void merge(Set<Pair<Value, ClassConstant>> in1, Set<Pair<Value, ClassConstant>> in2, Set<Pair<Value, ClassConstant>> out) {
        out.addAll(in1);
        out.addAll(in2);
    }

    @Override
    protected void copy(Set<Pair<Value, ClassConstant>> source, Set<Pair<Value, ClassConstant>> dest) {
        dest.addAll(source);
    }
}