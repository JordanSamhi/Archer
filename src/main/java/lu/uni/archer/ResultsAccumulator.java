package lu.uni.archer;

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

import lu.uni.archer.analysis.Analyses;
import lu.uni.archer.files.MethodsManager;
import lu.uni.archer.patch.ImplicitCallingRelationshipResolver;
import lu.uni.archer.patch.MethodBuilder;
import lu.uni.archer.utils.Constants;
import org.javatuples.Triplet;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultsAccumulator {

    private static ResultsAccumulator instance;

    private String appName;
    private long analysisElapsedTime;
    private long taintAnalysisElapsedTime;
    private long instrumentationElapsedTime;
    private int newEdgesInCG;
    private int newNodesInCG;
    private int numberOfExtraStmtCovered;
    private int numberOfStmtCovered;
    private boolean reachedTimeout;
    private int numberOfNodesInCG;
    private int numberOfEdgesInCG;

    private ResultsAccumulator() {
        this.setAppName("");
        this.setAnalysisElapsedTime(0);
        this.setNewNodesInCG(0);
        this.setInstrumentationElapsedTime(0);
        this.setTaintAnalysisElapsedTime(0);
        this.setNewEdgesInCG(0);
        this.setNumberOfExtraStmtCovered(0);
        this.setNumberOfStmtCovered(0);
        this.setNumberOfEdgesInCG(0);
        this.setNumberOfNodesInCG(0);
        this.setReachedTimeout(false);
    }

    public static ResultsAccumulator v() {
        if (instance == null) {
            instance = new ResultsAccumulator();
        }
        return instance;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void printVectorResults() {
        System.out.println(this.generateVector());
    }

    private String generateVector() {
        return String.format("%s;%d;%d;%d;%d;%d;%d;%d;%d;%.2f;%d;%s", this.getAppName(), this.getAnalysisElapsedTime(),
                this.getInstrumentationElapsedTime(), this.getTaintAnalysisElapsedTime(),
                this.newEdgesInCG, this.newNodesInCG, this.numberOfNodesInCG, this.numberOfEdgesInCG,
                this.numberOfExtraStmtCovered, (double) this.numberOfExtraStmtCovered * 100 / this.numberOfStmtCovered,
                this.reachedTimeout ? 1 : 0, this.generateContraintsVector());
    }

    public void printResults() {
        System.out.println("Results:");
        System.out.printf(" - App name: %s%n", this.getAppName());
        System.out.printf(" - Analysis elapsed time: %d%n", this.getAnalysisElapsedTime());
        System.out.printf(" - Instrumentation elapsed time: %d%n", this.getInstrumentationElapsedTime());
        System.out.printf(" - Taint Analysis elapsed time: %d%n", this.getTaintAnalysisElapsedTime());
        System.out.printf(" - Number of new Edges in call graph: %d%n", this.newEdgesInCG);
        System.out.printf(" - Number of new Nodes in call graph: %d%n", this.newNodesInCG);
        System.out.printf(" - Number of Edges in call graph: %d%n", this.numberOfEdgesInCG);
        System.out.printf(" - Number of Nodes in call graph: %d%n", this.numberOfNodesInCG);
        System.out.printf(" - Number of extra statement covered: %d%n", this.numberOfExtraStmtCovered);
        System.out.printf(" - Proportion of extra code covered: %.2f%% %n", (double) this.numberOfExtraStmtCovered * 100 / this.numberOfStmtCovered);
        System.out.printf(" - Reached timeout: %s%n", this.reachedTimeout ? "yes" : "no");
        for (Map.Entry<SootMethod, Triplet<Set<SootClass>, Stmt, SootMethod>> entry : ImplicitCallingRelationshipResolver.v().getExecutorToPotentialTargets().entrySet()) {
            Triplet<Set<SootClass>, Stmt, SootMethod> triplet = entry.getValue();
            SootMethod callee = entry.getKey();
            Set<SootClass> potentialClassTargets = triplet.getValue0();
            Stmt currentStmt = triplet.getValue1();
            SootMethod currentMethod = triplet.getValue2();
            InvokeExpr invokeExpr = currentStmt.getInvokeExpr();
            SootMethod newCallee = MethodBuilder.v().getOldToNewMethod().get(callee);
            Value arg = invokeExpr.getArg(MethodsManager.v().getExecutorArgPosition(newCallee));
            System.out.println(" - Constraints:");
            System.out.printf("   - Method call \"%s\" in methods %s:%n",
                    callee.getName(), currentMethod);
            System.out.println("    - Potential targets:");
            for (SootClass sc : potentialClassTargets) {
                System.out.printf("     - %s.%s%n", sc.getName(), MethodsManager.v().getExecutee(newCallee).getName());
            }
            InfoflowCFG icfg = new InfoflowCFG();
            List<Unit> successors = icfg.getSuccsOf(currentStmt);
            Unit succ;
            if (successors.size() > 0) {
                succ = successors.get(0);
            } else {
                succ = currentStmt;
            }
            Set<soot.toolkits.scalar.Pair<String, Value>> results = (Set<soot.toolkits.scalar.Pair<String, Value>>) Analyses.v().getResults(Constants.METHODS_CALLED_PROPAGATION, arg, succ);
            if (results.size() > 0) {
                System.out.println("    - Potential constraints:");
                for (soot.toolkits.scalar.Pair<String, Value> pair : results) {
                    String label = pair.getO1();
                    Value v = pair.getO2();
                    String value;
                    if (v == null) {
                        value = "Unknown";
                    } else {
                        value = v.toString();
                    }
                    if (v instanceof FieldRef) {
                        value = ((FieldRef) v).getField().getName();
                    }
                    System.out.printf("     - %s: %s%n", label, value);
                }
            } else {
                System.out.println("    - No constraints");
            }
        }
    }

    private String generateContraintsVector() {
        StringBuilder sb = new StringBuilder();
        StringBuilder tmp;
        for (Map.Entry<SootMethod, Triplet<Set<SootClass>, Stmt, SootMethod>> entry : ImplicitCallingRelationshipResolver.v().getExecutorToPotentialTargets().entrySet()) {
            if (sb.length() > 0) {
                sb.append("#");
            }
            tmp = new StringBuilder();
            Triplet<Set<SootClass>, Stmt, SootMethod> triplet = entry.getValue();
            SootMethod callee = entry.getKey();
            Set<SootClass> potentialClassTargets = triplet.getValue0();
            Stmt currentStmt = triplet.getValue1();
            SootMethod currentMethod = triplet.getValue2();
            InvokeExpr invokeExpr = currentStmt.getInvokeExpr();
            SootMethod newCallee = MethodBuilder.v().getOldToNewMethod().get(callee);
            Value arg = invokeExpr.getArg(MethodsManager.v().getExecutorArgPosition(newCallee));
            tmp.append(callee.getName());
            tmp.append("|");
            tmp.append(currentMethod);
            tmp.append("|");
            for (SootClass sc : potentialClassTargets) {
                tmp.append(String.format("%s.%s", sc.getName(), MethodsManager.v().getExecutee(newCallee).getName()));
                tmp.append("%");
            }
            tmp.append("|");
            InfoflowCFG icfg = new InfoflowCFG();
            List<Unit> successors = icfg.getSuccsOf(currentStmt);
            Unit succ;
            if (successors.size() > 0) {
                succ = successors.get(0);
            } else {
                succ = currentStmt;
            }
            Set<soot.toolkits.scalar.Pair<String, Value>> results = (Set<soot.toolkits.scalar.Pair<String, Value>>) Analyses.v().getResults(Constants.METHODS_CALLED_PROPAGATION, arg, succ);
            for (soot.toolkits.scalar.Pair<String, Value> pair : results) {
                String label = pair.getO1();
                Value v = pair.getO2();
                String value;
                if (v == null) {
                    value = "Unknown";
                } else {
                    value = v.toString();
                }
                if (v instanceof FieldRef) {
                    value = ((FieldRef) v).getField().getName();
                }
                tmp.append(label);
                tmp.append("&");
                tmp.append(value);
                tmp.append("%");
            }
            sb.append(tmp);
        }
        return sb.toString();
    }

    public long getAnalysisElapsedTime() {
        return analysisElapsedTime;
    }

    public void setAnalysisElapsedTime(long l) {
        this.analysisElapsedTime = l;
    }

    public long getTaintAnalysisElapsedTime() {
        return taintAnalysisElapsedTime;
    }

    public void setTaintAnalysisElapsedTime(long taintAnalysisElapsedTime) {
        this.taintAnalysisElapsedTime = taintAnalysisElapsedTime;
    }

    public long getInstrumentationElapsedTime() {
        return instrumentationElapsedTime;
    }

    public void setInstrumentationElapsedTime(long instrumentationElapsedTime) {
        this.instrumentationElapsedTime = instrumentationElapsedTime;
    }

    public void incrementNumberOfNewEdge() {
        this.newEdgesInCG++;
    }

    public void incrementNumberOfNewNodes() {
        this.newNodesInCG++;
    }

    public void addNumberOfExtraStmtCovered(int i) {
        this.numberOfExtraStmtCovered += i;
    }

    public void addNumberOfStmtCovered(int i) {
        this.numberOfStmtCovered += i;
    }

    public void setNewEdgesInCG(int newEdgesInCG) {
        this.newEdgesInCG = newEdgesInCG;
    }

    public void setNumberOfExtraStmtCovered(int i) {
        this.numberOfExtraStmtCovered = i;
    }

    public void setNumberOfStmtCovered(int i) {
        this.numberOfStmtCovered = i;
    }

    public int getNumberOfExtraStmtCovered() {
        return numberOfExtraStmtCovered;
    }

    public int getNumberOfStmtCovered() {
        return numberOfStmtCovered;
    }

    public boolean isReachedTimeout() {
        return reachedTimeout;
    }

    public void setReachedTimeout(boolean reachedTimeout) {
        this.reachedTimeout = reachedTimeout;
    }

    public int getNewNodesInCG() {
        return newNodesInCG;
    }

    public void setNewNodesInCG(int newNodesInCG) {
        this.newNodesInCG = newNodesInCG;
    }

    public int getNumberOfNodesInCG() {
        return numberOfNodesInCG;
    }

    public void setNumberOfNodesInCG(int numberOfNodesInCG) {
        this.numberOfNodesInCG = numberOfNodesInCG;
    }

    public int getNumberOfEdgesInCG() {
        return numberOfEdgesInCG;
    }

    public void setNumberOfEdgesInCG(int numberOfEdgesInCG) {
        this.numberOfEdgesInCG = numberOfEdgesInCG;
    }
}