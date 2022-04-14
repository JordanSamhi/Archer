package lu.uni.archer.analysis;

import heros.InterproceduralCFG;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.ide.JimpleIFDSSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyses {
    private final List<IFDSProblem> problems;
    private final Map<String, JimpleIFDSSolver<?, InterproceduralCFG<Unit, SootMethod>>> solvers;

    private static Analyses instance;

    public static Analyses v() {
        if (instance == null) {
            instance = new Analyses();
        }
        return instance;
    }

    private Analyses() {
        this.problems = new ArrayList<>();
        this.solvers = new HashMap<>();
    }

    public void addProblem(IFDSProblem p) {
        if (!this.problems.contains(p)) {
            this.problems.add(p);
        }
    }

    public void solveProblems() {
        JimpleIFDSSolver<?, InterproceduralCFG<Unit, SootMethod>> solver;
        for (IFDSProblem problem : this.problems) {
            solver = new JimpleIFDSSolver<>(problem);
            this.solvers.put(problem.getAnalysisName(), solver);
            solver.solve();
        }
    }

    public JimpleIFDSSolver<?, InterproceduralCFG<Unit, SootMethod>> getSolver(String analysisName) {
        if (this.solvers.containsKey(analysisName)) {
            return this.solvers.get(analysisName);
        }
        return null;
    }
}
