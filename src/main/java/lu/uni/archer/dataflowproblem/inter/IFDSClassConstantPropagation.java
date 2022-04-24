package lu.uni.archer.dataflowproblem.inter;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import lu.uni.archer.dataflowproblem.IFDSProblem;
import lu.uni.archer.files.ClassConstantMethodsManager;
import lu.uni.archer.files.CollectionMethodsToPropagateTypeManager;
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

public class IFDSClassConstantPropagation extends IFDSProblem<Pair<Value, ClassConstant>> {

    public IFDSClassConstantPropagation() {
        this(new JimpleBasedInterproceduralCFG());
    }

    public IFDSClassConstantPropagation(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override
    public String getProblemName() {
        return Constants.IFDS_CLASS_CONSTANT_PROPAGATION;
    }

    @Override
    public Set<ClassConstant> getResults(Value v, Unit u) {
        Set<ClassConstant> results = new HashSet<>();
        Set<Pair<Value, ClassConstant>> resultsComputed = (Set<Pair<Value, ClassConstant>>) this.solver.ifdsResultsAt(u);
        for (Pair<Value, ClassConstant> pair : resultsComputed) {
            if (pair.getO1().equals(v)) {
                results.add(pair.getO2());
            }
        }
        return results;
    }

    @Override
    protected FlowFunctions<Unit, Pair<Value, ClassConstant>, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Pair<Value, ClassConstant>, SootMethod>() {

            @Override
            public FlowFunction<Pair<Value, ClassConstant>> getNormalFlowFunction(Unit current, Unit succ) {
                if (current instanceof DefinitionStmt) {
                    DefinitionStmt ds = (DefinitionStmt) current;
                    if (ds.containsInvokeExpr()) {
                        return Identity.v();
                    }
                    final Value right = ds.getRightOp();
                    final Value left = ds.getLeftOp();
                    if (right instanceof ClassConstant) {
                        return new FlowFunction<Pair<Value, ClassConstant>>() {

                            @Override
                            public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                if (in.equals(zeroValue())) {
                                    Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                    set.add(zeroValue());
                                    set.add(new Pair<>(left, (ClassConstant) right));
                                    return set;
                                } else if (in.getO1().equivTo(left)) {
                                    return Collections.emptySet();
                                } else {
                                    return Collections.singleton(in);
                                }
                            }
                        };
                    } else if (right instanceof Ref || right instanceof Local) {
                        return new FlowFunction<Pair<Value, ClassConstant>>() {

                            @Override
                            public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                if (in.getO1().equivTo(left)) {
                                    return Collections.emptySet();
                                } else if (right.equivTo(in.getO1())) {
                                    Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                    set.add(new Pair<>(left, in.getO2()));
                                    set.add(in);
                                    return set;
                                } else {
                                    return Collections.singleton(in);
                                }
                            }
                        };
                    } else if (right instanceof CastExpr) {
                        CastExpr ce = (CastExpr) right;
                        Value v = ce.getOp();
                        return new FlowFunction<Pair<Value, ClassConstant>>() {

                            @Override
                            public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                if (in.getO1().equivTo(v)) {
                                    Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                    set.add(new Pair<>(left, in.getO2()));
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
                return Identity.v();
            }

            @Override
            public FlowFunction<Pair<Value, ClassConstant>> getCallFlowFunction(Unit current, SootMethod target) {
                InvokeExpr ie = ((Stmt) current).getInvokeExpr();
                final List<Value> args = ie.getArgs();
                final List<Local> params = new ArrayList<>();
                for (int i = 0; i < target.getParameterCount(); i++) {
                    params.add(target.getActiveBody().getParameterLocal(i));
                }
                return new FlowFunction<Pair<Value, ClassConstant>>() {
                    @Override
                    public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
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
            public FlowFunction<Pair<Value, ClassConstant>> getReturnFlowFunction(Unit callSite, SootMethod callee, Unit exit, Unit returnSite) {
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
                                return new FlowFunction<Pair<Value, ClassConstant>>() {
                                    @Override
                                    public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                        if (in.getO1().equivTo(retLocal)) {
                                            return Collections.singleton(new Pair<>(tgtLocal, in.getO2()));
                                        }
                                        return Collections.emptySet();
                                    }

                                };
                            } else if (op instanceof ClassConstant) {
                                return new FlowFunction<Pair<Value, ClassConstant>>() {
                                    @Override
                                    public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                        if (in.equals(zeroValue())) {
                                            return Collections.singleton(new Pair<>(tgtLocal, (ClassConstant) op));
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
            public FlowFunction<Pair<Value, ClassConstant>> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
                if (callSite instanceof InvokeStmt) {
                    InvokeStmt is = (InvokeStmt) callSite;
                    InvokeExpr ie = is.getInvokeExpr();
                    SootMethod callee = ie.getMethod();
                    if (ie instanceof InstanceInvokeExpr) {
                        InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                        Value base = iie.getBase();
                        if (ClassConstantMethodsManager.v().isInMethodsThatGenerateToBase(callee)) {
                            Value classConstant = ClassConstantMethodsManager.v().getClassConstant(ie);
                            if (classConstant != null) {
                                if (classConstant instanceof ClassConstant) {
                                    return new FlowFunction<Pair<Value, ClassConstant>>() {

                                        @Override
                                        public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                            if (in.equals(zeroValue())) {
                                                Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                                set.add(zeroValue());
                                                set.add(new Pair<>(base, (ClassConstant) classConstant));
                                                return set;
                                            } else if (in.getO1().equivTo(base)) {
                                                return Collections.emptySet();
                                            } else {
                                                return Collections.singleton(in);
                                            }
                                        }
                                    };
                                } else if (classConstant instanceof Ref || classConstant instanceof Local) {
                                    return new FlowFunction<Pair<Value, ClassConstant>>() {

                                        @Override
                                        public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                            if (in.getO1().equivTo(base)) {
                                                return Collections.emptySet();
                                            } else if (classConstant.equivTo(in.getO1())) {
                                                Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                                set.add(new Pair<>(base, in.getO2()));
                                                set.add(in);
                                                return set;
                                            } else {
                                                return Collections.singleton(in);
                                            }
                                        }
                                    };
                                }
                            }
                        } else if (CollectionMethodsToPropagateTypeManager.v().isCollectionMethodToPropagatyeType(callee)) {
                            int argPosition = CollectionMethodsToPropagateTypeManager.v().getArgPosition(callee);
                            if (argPosition != -1) {
                                Value arg = iie.getArg(argPosition);
                                return new FlowFunction<Pair<Value, ClassConstant>>() {

                                    @Override
                                    public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                        if (in.getO1().equivTo(arg)) {
                                            Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                            set.add(in);
                                            set.add(new Pair<>(base, in.getO2()));
                                            return set;
                                        } else if (in.getO1().equivTo(base)) {
                                            return Collections.emptySet();
                                        } else {
                                            return Collections.singleton(in);
                                        }
                                    }
                                };
                            }
                        }
                    }
                } else if (callSite instanceof DefinitionStmt) {
                    DefinitionStmt ds = (DefinitionStmt) callSite;
                    Value left = ds.getLeftOp();
                    if (ds.containsInvokeExpr()) {
                        InvokeExpr ie = ds.getInvokeExpr();
                        SootMethod callee = ie.getMethod();
                        if (ClassConstantMethodsManager.v().isInMethodsThatPropagateBaseToReceiver(callee)) {
                            if (ie instanceof InstanceInvokeExpr) {
                                InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                                Value base = iie.getBase();
                                return new FlowFunction<Pair<Value, ClassConstant>>() {

                                    @Override
                                    public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                        if (in.getO1().equivTo(base)) {
                                            Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                            set.add(new Pair<>(left, in.getO2()));
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
                return Identity.v();
            }
        };
    }

    @Override
    protected Pair<Value, ClassConstant> createZeroValue() {
        return new Pair<>(Jimple.v().newLocal("<<zero>>", UnknownType.v()), ClassConstant.v("<<zero>>"));
    }

    @Override
    public Map<Unit, Set<Pair<Value, ClassConstant>>> initialSeeds() {
        return DefaultSeeds.make(Collections.singleton(Scene.v().getEntryPoints().get(0).getActiveBody().getUnits().getFirst()), zeroValue());
    }
}