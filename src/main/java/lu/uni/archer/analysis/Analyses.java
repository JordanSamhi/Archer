package lu.uni.archer.analysis;

import lu.uni.archer.dataflowproblem.Problem;
import soot.Unit;
import soot.Value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Analyses {
    private final List<Problem> problems;
//    private final Map<String, JimpleIFDSSolver<?, InterproceduralCFG<Unit, SootMethod>>> IFDSSolvers;
//    private final Map<String, IntraProblem> intraSolvers;

    private static Analyses instance;

    public static Analyses v() {
        if (instance == null) {
            instance = new Analyses();
        }
        return instance;
    }

    private Analyses() {
        this.problems = new ArrayList<>();
//        this.IFDSSolvers = new HashMap<>();
//        this.intraSolvers = new HashMap<>();
    }

    public void addProblem(Problem p) {
        if (!this.problems.contains(p)) {
            this.problems.add(p);
        }
    }

    public void solveProblems() {
        for (Problem problem : this.problems) {
            problem.solve();
        }
    }

    public Set<?> getResults(String problemName, Value v, Unit u) {
        String IFDSProblemName = String.format("IFDS%s", problemName);
        String INTRAProblemName = String.format("INTRA%s", problemName);
        Set<Object> results = new HashSet<>();
        Problem ifdsProblem = this.getProblem(IFDSProblemName);
        Problem intraProblem = this.getProblem(INTRAProblemName);
        if (ifdsProblem != null) {
            results.addAll(ifdsProblem.getResults(v, u));
        }
        if (intraProblem != null) {
            results.addAll(intraProblem.getResults(v, u));
        }
        return results;
    }

    private Problem getProblem(String problemName) {
        for (Problem p : this.problems) {
            if (p.getProblemName().equals(problemName)) {
                return p;
            }
        }
        return null;
    }
}