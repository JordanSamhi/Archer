package lu.uni.archer.analysis;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
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

public class IFDSFieldPropagation extends IFDSProblem<Pair<Local, FieldRef>> {

    public IFDSFieldPropagation() {
        this(new JimpleBasedInterproceduralCFG());
    }

    public IFDSFieldPropagation(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override
    protected FlowFunctions<Unit, Pair<Local, FieldRef>, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Pair<Local, FieldRef>, SootMethod>() {

            @Override
            public FlowFunction<Pair<Local, FieldRef>> getNormalFlowFunction(Unit current, Unit succ) {
                if (current instanceof DefinitionStmt) {
                    DefinitionStmt ds = (DefinitionStmt) current;
                    if (ds.containsInvokeExpr()) {
                        return Identity.v();
                    }
                    final Value right = ds.getRightOp();
                    final Value left = ds.getLeftOp();
                    if (left instanceof Local) {
                        final Local leftLocal = (Local) left;
                        if (right instanceof FieldRef) {
                            FieldRef fr = (FieldRef) right;
                            return new FlowFunction<Pair<Local, FieldRef>>() {

                                @Override
                                public Set<Pair<Local, FieldRef>> computeTargets(Pair<Local, FieldRef> in) {
                                    if (in.equals(zeroValue())) {
                                        Set<Pair<Local, FieldRef>> set = new HashSet<>();
                                        set.add(new Pair<>(leftLocal, fr));
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
                            return new FlowFunction<Pair<Local, FieldRef>>() {

                                @Override
                                public Set<Pair<Local, FieldRef>> computeTargets(Pair<Local, FieldRef> in) {
                                    if (in.getO1().equivTo(left)) {
                                        return Collections.emptySet();
                                    } else if (right.equivTo(in.getO1())) {
                                        Set<Pair<Local, FieldRef>> set = new HashSet<>();
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
            public FlowFunction<Pair<Local, FieldRef>> getCallFlowFunction(Unit current, SootMethod target) {
                InvokeExpr ie = ((Stmt) current).getInvokeExpr();
                final List<Value> args = ie.getArgs();
                final List<Local> params = new ArrayList<>();
                for (int i = 0; i < target.getParameterCount(); i++) {
                    params.add(target.getActiveBody().getParameterLocal(i));
                }
                return new FlowFunction<Pair<Local, FieldRef>>() {
                    @Override
                    public Set<Pair<Local, FieldRef>> computeTargets(Pair<Local, FieldRef> in) {
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
            public FlowFunction<Pair<Local, FieldRef>> getReturnFlowFunction(Unit callSite, SootMethod callee, Unit exit, Unit returnSite) {
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
                                return new FlowFunction<Pair<Local, FieldRef>>() {
                                    @Override
                                    public Set<Pair<Local, FieldRef>> computeTargets(Pair<Local, FieldRef> in) {
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
            public FlowFunction<Pair<Local, FieldRef>> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
                return Identity.v();
            }
        };
    }

    @Override
    protected Pair<Local, FieldRef> createZeroValue() {
        return new Pair<>(Jimple.v().newLocal("<<zero>>", NullType.v()), null);
    }

    @Override
    public String getAnalysisName() {
        return Constants.FIELD_PROPAGATION;
    }


    @Override
    public Map<Unit, Set<Pair<Local, FieldRef>>> initialSeeds() {
        return DefaultSeeds.make(Collections.singleton(Scene.v().getEntryPoints().get(0).getActiveBody().getUnits().getFirst()), zeroValue());
    }
}
