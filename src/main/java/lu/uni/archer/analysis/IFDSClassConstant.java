package lu.uni.archer.analysis;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import lu.uni.archer.utils.ClassConstantMethods;
import soot.*;
import soot.jimple.*;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
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

// TODO: refactor

public class IFDSClassConstant extends DefaultJimpleIFDSTabulationProblem<Pair<Value, ClassConstant>, InterproceduralCFG<Unit, SootMethod>> {

    public IFDSClassConstant(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
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
                        if (idx >= 0) {
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
                    if (ClassConstantMethods.v().containsMethodUsingClassConstant(callee)) {
                        SpecialInvokeExpr sie = (SpecialInvokeExpr) ie;
                        Value base = sie.getBase();
                        Value v = ClassConstantMethods.v().getClassConstant(ie);
                        if (v != null) {
                            if (v instanceof ClassConstant) {
                                return new FlowFunction<Pair<Value, ClassConstant>>() {

                                    @Override
                                    public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                        if (in.equals(zeroValue())) {
                                            Set<Pair<Value, ClassConstant>> set = new HashSet<>();
                                            set.add(zeroValue());
                                            set.add(new Pair<>(base, (ClassConstant) v));
                                            return set;
                                        } else if (in.getO1().equivTo(base)) {
                                            return Collections.emptySet();
                                        } else {
                                            return Collections.singleton(in);
                                        }
                                    }
                                };
                            } else if (v instanceof Ref || v instanceof Local) {
                                return new FlowFunction<Pair<Value, ClassConstant>>() {

                                    @Override
                                    public Set<Pair<Value, ClassConstant>> computeTargets(Pair<Value, ClassConstant> in) {
                                        if (in.getO1().equivTo(base)) {
                                            return Collections.emptySet();
                                        } else if (v.equivTo(in.getO1())) {
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
                    }
                } else if (callSite instanceof DefinitionStmt) {
                    DefinitionStmt ds = (DefinitionStmt) callSite;
                    Value left = ds.getLeftOp();
                    Value right = ds.getRightOp();
                    if (right instanceof InvokeExpr) {
                        InvokeExpr ie = (InvokeExpr) right;
                        SootMethod callee = ie.getMethod();
                        if (ClassConstantMethods.v().containsPropagationMethods(callee)) {
                            VirtualInvokeExpr vie = (VirtualInvokeExpr) ie;
                            Value base = vie.getBase();
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
