package lu.uni.archer.analysis;

import heros.solver.Pair;
import lu.uni.archer.files.SourcesSinksManager;
import lu.uni.archer.utils.Writer;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.util.MultiMap;

import java.util.Iterator;

public class FlowAnalysis {
    private final SetupApplication sa;
    private InfoflowResults ir;

    public FlowAnalysis(SetupApplication sa) {
        this.sa = sa;
    }

    public void runTaintAnalysis() {
        try {
            this.ir = sa.runInfoflow(SourcesSinksManager.v().getSources(), SourcesSinksManager.v().getSinks());
        } catch (Exception ignored) {
        }
    }

    public void printResults() {
        InfoflowCFG icfg = new InfoflowCFG();
        MultiMap<ResultSinkInfo, ResultSourceInfo> res = this.ir.getResults();
        Iterator<Pair<ResultSinkInfo, ResultSourceInfo>> it = res.iterator();
        Pair<ResultSinkInfo, ResultSourceInfo> next;
        ResultSinkInfo sink;
        ResultSourceInfo source;
        Writer.v().psuccess("Flowdroid found leaks:");
        int i = 0;
        while (it.hasNext()) {
            Writer.v().pinfo(String.format("Leak %d: ", ++i));
            next = it.next();
            source = next.getO2();
            sink = next.getO1();
            System.out.println("    From: " + source.getStmt());
            System.out.println("    To: " + sink.getStmt());
            Stmt[] path = source.getPath();
            if (path != null) {
                System.out.println("    Detailed path:");
                for (Stmt s : path) {
                    System.out.println("      -" + s + " => in method: " + icfg.getMethodOf(s));
                }
            }
        }
    }
}