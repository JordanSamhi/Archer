package lu.uni.archer.dataflowproblem.inter;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import lu.uni.archer.analysis.Analyses;
import lu.uni.archer.dataflowproblem.IFDSProblem;
import lu.uni.archer.files.MethodCallMethodsManager;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.utils.Constants;
import soot.*;
import soot.jimple.*;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.toolkits.scalar.Pair;

import java.util.*;

/*-
 * #%L
 * Archer
 *
 * %%
 * Copyright (C) 2022 Jordan Samhi
 * University of Luxembourg - Interdisciplinary Centre for
 * Security Reliability and Trust (SnT) - TruX - All rights reserved
 *
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

public class IFDSMethodsCalledPropagation extends IFDSProblem<Pair<Value, Pair<String, Value>>> {

    public IFDSMethodsCalledPropagation() {
        this(new JimpleBasedInterproceduralCFG());
    }

    public IFDSMethodsCalledPropagation(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override
    public String getProblemName() {
        return Constants.IFDS_METHODS_CALLED_PROPAGATION;
    }

    @Override
    public Set<Pair<String, Value>> getResults(Value v, Unit u) {
        Set<Pair<String, Value>> results = new HashSet<>();
        Set<Pair<Value, Pair<String, Value>>> resultsComputed = (Set<Pair<Value, Pair<String, Value>>>) this.solver.ifdsResultsAt(u);
        for (Pair<Value, Pair<String, Value>> pair : resultsComputed) {
            if (pair.getO1().equals(v)) {
                results.add(pair.getO2());
            }
        }
        return results;
    }

    @Override
    protected FlowFunctions<Unit, Pair<Value, Pair<String, Value>>, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Pair<Value, Pair<String, Value>>, SootMethod>() {

            @Override
            public FlowFunction<Pair<Value, Pair<String, Value>>> getNormalFlowFunction(Unit current, Unit succ) {
                if (current instanceof DefinitionStmt) {
                    DefinitionStmt ds = (DefinitionStmt) current;
                    if (ds.containsInvokeExpr()) {
                        return Identity.v();
                    }
                    final Value right = ds.getRightOp();
                    final Value left = ds.getLeftOp();
                    if (left instanceof Local) {
                        final Local leftLocal = (Local) left;
                        if (right instanceof CastExpr) {
                            CastExpr ce = (CastExpr) right;
                            Value v = ce.getOp();
                            return new FlowFunction<Pair<Value, Pair<String, Value>>>() {

                                @Override
                                public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                    if (in.getO1().equivTo(v)) {
                                        Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                        set.add(new Pair<>(leftLocal, in.getO2()));
                                        set.add(in);
                                        return set;
                                    } else if (left.equivTo(in.getO1())) {
                                        return Collections.emptySet();
                                    } else {
                                        return Collections.singleton(in);
                                    }
                                }
                            };
                        } else if (right instanceof Local) {
                            return new FlowFunction<Pair<Value, Pair<String, Value>>>() {

                                @Override
                                public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                    if (in.getO1().equivTo(left)) {
                                        return Collections.emptySet();
                                    } else if (right.equivTo(in.getO1())) {
                                        Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                        set.add(new Pair<>(leftLocal, in.getO2()));
                                        set.add(in);
                                        return set;
                                    } else {
                                        return Collections.singleton(in);
                                    }
                                }
                            };
                        }
                    }
                }
                return Identity.v();
            }

            @Override
            public FlowFunction<Pair<Value, Pair<String, Value>>> getCallFlowFunction(Unit current, SootMethod target) {
                InvokeExpr ie = ((Stmt) current).getInvokeExpr();
                final List<Value> args = ie.getArgs();
                final List<Local> params = new ArrayList<>();
                for (int i = 0; i < target.getParameterCount(); i++) {
                    params.add(target.getActiveBody().getParameterLocal(i));
                }
                return new FlowFunction<Pair<Value, Pair<String, Value>>>() {
                    @Override
                    public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                        Value v = in.getO1();
                        int idx = args.indexOf(v);
                        if (idx >= 0 && params.size() > idx) {
                            return Collections.singleton(new Pair<>(params.get(idx), in.getO2()));
                        }
                        return Collections.emptySet();
                    }
                };
            }

            @Override
            public FlowFunction<Pair<Value, Pair<String, Value>>> getReturnFlowFunction(Unit callSite, SootMethod callee, Unit exit, Unit returnSite) {
                if (exit instanceof ReturnStmt) {
                    ReturnStmt returnStmt = (ReturnStmt) exit;
                    Value op = returnStmt.getOp();
                    if (callSite instanceof DefinitionStmt) {
                        DefinitionStmt ds = (DefinitionStmt) callSite;
                        Value lOp = ds.getLeftOp();
                        if (lOp instanceof Local) {
                            final Local tgtLocal = (Local) lOp;
                            if (op instanceof Local) {
                                final Local retLocal = (Local) op;
                                return new FlowFunction<Pair<Value, Pair<String, Value>>>() {
                                    @Override
                                    public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                        if (in.getO1().equivTo(retLocal)) {
                                            return Collections.singleton(new Pair<>(tgtLocal, in.getO2()));
                                        }
                                        return Collections.emptySet();
                                    }

                                };
                            }
                        }
                    }
                }
                return KillAll.v();
            }

            @Override
            public FlowFunction<Pair<Value, Pair<String, Value>>> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
                if (callSite instanceof DefinitionStmt) {
                    DefinitionStmt ds = (DefinitionStmt) callSite;
                    Value left = ds.getLeftOp();
                    if (left instanceof Local) {
                        Local leftLocal = (Local) left;
                        if (ds.containsInvokeExpr()) {
                            InvokeExpr ie = ds.getInvokeExpr();
                            SootMethod callee = ie.getMethod();
                            if (ie instanceof InstanceInvokeExpr) {
                                InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                                Value base = iie.getBase();
                                if (MethodCallMethodsManager.v().isInMethodsThatGenerateBaseToReceiver(callee)) {
                                    Value valueInMethodCallTmp = MethodCallMethodsManager.v().getMethodCallParameter(ie);
                                    if (!(valueInMethodCallTmp instanceof Constant)) {
                                        Set<FieldRef> potentialFields = (Set<FieldRef>) Analyses.v().getResults(Constants.FIELD_PROPAGATION, valueInMethodCallTmp, callSite);
                                        List<FieldRef> potentialFieldsList = new ArrayList<>(potentialFields);
                                        if (potentialFieldsList.size() == 1) {
                                            valueInMethodCallTmp = potentialFieldsList.get(0);
                                        } else {
                                            valueInMethodCallTmp = null;
                                        }
                                    }
                                    Value valueInMethodCall = valueInMethodCallTmp;
                                    return new FlowFunction<Pair<Value, Pair<String, Value>>>() {

                                        @Override
                                        public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                            if (!base.equals(left)) {
                                                if (in.equals(zeroValue())) {
                                                    Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                                    set.add(new Pair<>(leftLocal, new Pair<>(MethodCallMethodsManager.v().getLabel(callee), valueInMethodCall)));
                                                    if (base instanceof Local) {
                                                        set.add(new Pair<>(base, new Pair<>(MethodCallMethodsManager.v().getLabel(callee), valueInMethodCall)));
                                                    }
                                                    set.add(in);
                                                    return set;
                                                } else if (left.equivTo(in.getO1())) {
                                                    return Collections.emptySet();
                                                } else {
                                                    return Collections.singleton(in);
                                                }
                                            } else {
                                                Pair<String, Value> pair = new Pair<>(MethodCallMethodsManager.v().getLabel(callee), valueInMethodCall);
                                                if (in.equals(zeroValue())) {
                                                    Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                                    set.add(new Pair<>(leftLocal, pair)); // TODO: update to real value
                                                    set.add(in);
                                                    return set;
                                                } else if (left.equivTo(in.getO1()) && in.getO2().equals(pair)) {
                                                    return Collections.emptySet();
                                                } else {
                                                    return Collections.singleton(in);
                                                }
                                            }
                                        }
                                    };
                                } else if (MethodCallMethodsManager.v().isInMethodsThatPropagateBaseToReceiver(callee)) {
                                    return new FlowFunction<Pair<Value, Pair<String, Value>>>() {

                                        @Override
                                        public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                            if (in.getO1().equivTo(base)) {
                                                Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                                set.add(new Pair<>(leftLocal, in.getO2()));
                                                set.add(in);
                                                return set;
                                            } else if (left.equivTo(in.getO1())) {
                                                return Collections.emptySet();
                                            } else {
                                                return Collections.singleton(in);
                                            }
                                        }
                                    };
                                } else if (MethodCallMethodsManager.v().isInMethodsThatPropagateParameterToReceiver(callee)) {
                                    return new FlowFunction<Pair<Value, Pair<String, Value>>>() {

                                        @Override
                                        public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                            Value v = MethodCallMethodsManager.v().getMethodCallParameter(ie);
                                            if (in.getO1().equivTo(v)) {
                                                Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                                set.add(new Pair<>(leftLocal, in.getO2()));
                                                set.add(in);
                                                return set;
                                            } else if (left.equivTo(in.getO1())) {
                                                return Collections.emptySet();
                                            } else {
                                                return Collections.singleton(in);
                                            }
                                        }
                                    };
                                }
                            }
                        }
                    }
                } else if (callSite instanceof InvokeStmt) {

                    InvokeStmt is = (InvokeStmt) callSite;
                    InvokeExpr ie = is.getInvokeExpr();
                    SootMethod callee = ie.getMethod();
                    if (MethodCallMethodsManager.v().isInMethodsThatGenerateBaseToReceiver(callee)) {
                        if (ie instanceof InstanceInvokeExpr) {
                            InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                            Value base = iie.getBase();
                            if (base instanceof Local) {
                                return new FlowFunction<Pair<Value, Pair<String, Value>>>() {

                                    @Override
                                    public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                        if (in.equals(zeroValue())) {
                                            Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                            set.add(new Pair<>(base, new Pair<>(MethodCallMethodsManager.v().getLabel(callee), MethodCallMethodsManager.v().getMethodCallParameter(ie)))); // TODO: update to real value
                                            set.add(in);
                                            return set;
                                        } else {
                                            return Collections.singleton(in);
                                        }
                                    }
                                };
                            }
                        }
                    } else if (MethodCallMethodsManager.v().isInMethodsThatAreExecutorsAndSetConstraints(callee)) {
                        Map<Integer, String> labels = MethodCallMethodsManager.v().getLabels(callee);
                        return new FlowFunction<Pair<Value, Pair<String, Value>>>() {

                            @Override
                            public Set<Pair<Value, Pair<String, Value>>> computeTargets(Pair<Value, Pair<String, Value>> in) {
                                Value argToPropagateTo = ie.getArg(MethodsManager.v().getExecutorArgPosition(callee));
                                if (in.equals(zeroValue())) {
                                    Set<Pair<Value, Pair<String, Value>>> set = new HashSet<>();
                                    for (Map.Entry<Integer, String> entry : labels.entrySet()) {
                                        int argPosition = entry.getKey();
                                        String label = entry.getValue();
                                        Value v = ie.getArg(argPosition);
                                        if (!(v instanceof Constant)) {
                                            Set<FieldRef> potentialFields = (Set<FieldRef>) Analyses.v().getResults(Constants.FIELD_PROPAGATION, v, callSite);
                                            List<FieldRef> potentialFieldsList = new ArrayList<>(potentialFields);
                                            if (potentialFieldsList.size() == 1) {
                                                v = potentialFieldsList.get(0);
                                            } else {
                                                v = null;
                                            }
                                        }
                                        Pair<String, Value> pair = new Pair<>(label, v);
                                        set.add(new Pair<>(argToPropagateTo, pair));
                                    }
                                    return set;
                                } else if (argToPropagateTo.equivTo(in.getO1())) {
                                    return Collections.emptySet();
                                } else {
                                    return Collections.singleton(in);
                                }
                            }
                        };
                    }
                }
                return Identity.v();
            }
        };
    }

    @Override
    protected Pair<Value, Pair<String, Value>> createZeroValue() {
        return new Pair<>(Jimple.v().newLocal("<<zero>>", NullType.v()), new Pair<>("<<zero>>", IntConstant.v(0)));
    }

    @Override
    public Map<Unit, Set<Pair<Value, Pair<String, Value>>>> initialSeeds() {
        return DefaultSeeds.make(Collections.singleton(Scene.v().getEntryPoints().get(0).getActiveBody().getUnits().getFirst()), zeroValue());
    }
}
