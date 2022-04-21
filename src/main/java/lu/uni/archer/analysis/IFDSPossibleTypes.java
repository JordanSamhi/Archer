package lu.uni.archer.analysis;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import lu.uni.archer.files.CollectionMethodsToPropagateTypeManager;
import lu.uni.archer.utils.Constants;
import soot.*;
import soot.jimple.*;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.toolkits.scalar.Pair;

import java.util.*;

/**
 * This class is based on: https://github.com/soot-oss/soot/blob/develop/src/main/java/soot/jimple/toolkits/ide/exampleproblems/IFDSPossibleTypes.java
 */

public class IFDSPossibleTypes extends IFDSProblem<Pair<Value, Type>> {

    public IFDSPossibleTypes() {
        this(new JimpleBasedInterproceduralCFG());
    }

    public IFDSPossibleTypes(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override
    public String getAnalysisName() {
        return Constants.POSSIBLE_TYPES;
    }

    public FlowFunctions<Unit, Pair<Value, Type>, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Pair<Value, Type>, SootMethod>() {

            public FlowFunction<Pair<Value, Type>> getNormalFlowFunction(Unit src, Unit dest) {
                if (src instanceof DefinitionStmt) {
                    DefinitionStmt defnStmt = (DefinitionStmt) src;
                    if (defnStmt.containsInvokeExpr()) {
                        return Identity.v();
                    }
                    final Value right = defnStmt.getRightOp();
                    final Value left = defnStmt.getLeftOp();
                    if (right.getType() instanceof PrimType) {
                        return Identity.v();
                    }

                    if (right instanceof Constant || right instanceof NewExpr) {
                        return new FlowFunction<Pair<Value, Type>>() {
                            public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> source) {
                                if (source == zeroValue()) {
                                    Set<Pair<Value, Type>> res = new LinkedHashSet<>();
                                    res.add(new Pair<>(left, right.getType()));
                                    res.add(zeroValue());
                                    return res;
                                } else if (source.getO1() instanceof Local && source.getO1().equivTo(left)) {
                                    return Collections.emptySet();
                                } else {
                                    return Collections.singleton(source);
                                }
                            }
                        };
                    } else if (right instanceof Ref || right instanceof Local) {
                        return new FlowFunction<Pair<Value, Type>>() {
                            public Set<Pair<Value, Type>> computeTargets(final Pair<Value, Type> source) {
                                Value value = source.getO1();
                                if (source.getO1() instanceof Local && source.getO1().equivTo(left)) {
                                    return Collections.emptySet();
                                } else if (maybeSameLocation(value, right)) {
                                    return new LinkedHashSet<Pair<Value, Type>>() {
                                        {
                                            add(new Pair<>(left, source.getO2()));
                                            add(source);
                                        }
                                    };
                                } else {
                                    return Collections.singleton(source);
                                }
                            }

                            private boolean maybeSameLocation(Value v1, Value v2) {
                                if (!(v1 instanceof InstanceFieldRef && v2 instanceof InstanceFieldRef)
                                        && !(v1 instanceof ArrayRef && v2 instanceof ArrayRef)) {
                                    return v1.equivTo(v2);
                                }
                                if (v1 instanceof InstanceFieldRef && v2 instanceof InstanceFieldRef) {
                                    InstanceFieldRef ifr1 = (InstanceFieldRef) v1;
                                    InstanceFieldRef ifr2 = (InstanceFieldRef) v2;
                                    if (!ifr1.getField().getName().equals(ifr2.getField().getName())) {
                                        return false;
                                    }

                                    Local base1 = (Local) ifr1.getBase();
                                    Local base2 = (Local) ifr2.getBase();
                                    PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
                                    PointsToSet pts1 = pta.reachingObjects(base1);
                                    PointsToSet pts2 = pta.reachingObjects(base2);
                                    return pts1.hasNonEmptyIntersection(pts2);
                                } else {
                                    ArrayRef ar1 = (ArrayRef) v1;
                                    ArrayRef ar2 = (ArrayRef) v2;

                                    Local base1 = (Local) ar1.getBase();
                                    Local base2 = (Local) ar2.getBase();
                                    PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
                                    PointsToSet pts1 = pta.reachingObjects(base1);
                                    PointsToSet pts2 = pta.reachingObjects(base2);
                                    return pts1.hasNonEmptyIntersection(pts2);
                                }
                            }
                        };
                    }
                }
                return Identity.v();
            }

            public FlowFunction<Pair<Value, Type>> getCallFlowFunction(final Unit src, final SootMethod dest) {
                Stmt stmt = (Stmt) src;
                InvokeExpr ie = stmt.getInvokeExpr();
                final List<Value> callArgs = ie.getArgs();
                final List<Local> paramLocals = new ArrayList<>();
                for (int i = 0; i < dest.getParameterCount(); i++) {
                    paramLocals.add(dest.getActiveBody().getParameterLocal(i));
                }
                return new FlowFunction<Pair<Value, Type>>() {
                    public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> source) {
                        if (!dest.getName().equals("<clinit>") && !dest.getSubSignature().equals("void run()")) {
                            Value value = source.getO1();
                            int argIndex = callArgs.indexOf(value);
                            if (argIndex > -1) {
                                return Collections.singleton(new Pair<>(paramLocals.get(argIndex), source.getO2()));
                            }
                        }
                        return Collections.emptySet();
                    }
                };
            }

            public FlowFunction<Pair<Value, Type>> getReturnFlowFunction(Unit callSite, SootMethod callee, Unit exitStmt,
                                                                         Unit retSite) {
                if (exitStmt instanceof ReturnStmt) {
                    ReturnStmt returnStmt = (ReturnStmt) exitStmt;
                    Value op = returnStmt.getOp();
                    if (op instanceof Local) {
                        if (callSite instanceof DefinitionStmt) {
                            DefinitionStmt defnStmt = (DefinitionStmt) callSite;
                            Value leftOp = defnStmt.getLeftOp();
                            if (leftOp instanceof Local) {
                                final Local tgtLocal = (Local) leftOp;
                                final Local retLocal = (Local) op;
                                return new FlowFunction<Pair<Value, Type>>() {

                                    public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> source) {
                                        if (source.getO1() == retLocal) {
                                            return Collections.singleton(new Pair<>(tgtLocal, source.getO2()));
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

            public FlowFunction<Pair<Value, Type>> getCallToReturnFlowFunction(Unit call, Unit returnSite) {
                Stmt s = (Stmt) call;
                if (s.containsInvokeExpr()) {
                    InvokeExpr ie = (InvokeExpr) s.getInvokeExpr();
                    SootMethod callee = ie.getMethod();
                    if (ie instanceof InstanceInvokeExpr) {
                        InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                        Value base = iie.getBase();
                        if (CollectionMethodsToPropagateTypeManager.v().isCollectionMethodToPropagatyeType(callee)) {
                            int argPosition = CollectionMethodsToPropagateTypeManager.v().getArgPosition(callee);
                            if (argPosition != -1) {
                                Value arg = iie.getArg(argPosition);
                                return new FlowFunction<Pair<Value, Type>>() {

                                    @Override
                                    public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> in) {
                                        if (in.getO1().equivTo(arg)) {
                                            Set<Pair<Value, Type>> set = new HashSet<>();
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
                }
                return Identity.v();
            }
        };
    }

    public Map<Unit, Set<Pair<Value, Type>>> initialSeeds() {
        return DefaultSeeds.make(Collections.singleton(Scene.v().getEntryPoints().get(0).getActiveBody().getUnits().getFirst()),
                zeroValue());
    }

    public Pair<Value, Type> createZeroValue() {
        return new Pair<>(Jimple.v().newLocal("<dummy>", UnknownType.v()), UnknownType.v());
    }
}