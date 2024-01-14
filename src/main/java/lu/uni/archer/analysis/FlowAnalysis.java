package lu.uni.archer.analysis;

import heros.solver.Pair;
import lu.uni.archer.files.ExecuteesManager;
import lu.uni.archer.files.SourcesSinksManager;
import lu.uni.archer.utils.Writer;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.util.MultiMap;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FlowAnalysis {
    private final SetupApplication sa;
    private InfoflowResults ir;

    private InfoflowCFG icfg;

    public FlowAnalysis(SetupApplication sa) {
        this.sa = sa;
        sa.getConfig().setSootIntegrationMode(InfoflowAndroidConfiguration.SootIntegrationMode.UseExistingCallgraph);
        sa.getConfig().getPathConfiguration().setPathReconstructionMode(InfoflowAndroidConfiguration.PathReconstructionMode.Precise);
        sa.getConfig().setCodeEliminationMode(InfoflowAndroidConfiguration.CodeEliminationMode.NoCodeElimination);
        this.icfg = new InfoflowCFG();
    }

    public void runTaintAnalysis() {
        try {
            this.ir = sa.runInfoflow(SourcesSinksManager.v().getSources(), SourcesSinksManager.v().getSinks());
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void printResults() {
        InfoflowCFG icfg = new InfoflowCFG();
        MultiMap<ResultSinkInfo, ResultSourceInfo> res = this.ir.getResults();
        if (res != null) {
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
                System.out.println("    From: " + source.getStmt() + " in method: " + icfg.getMethodOf(source.getStmt()));
                System.out.println("    To: " + sink.getStmt() + " in method: " + icfg.getMethodOf(sink.getStmt()));
                Stmt[] path = source.getPath();
                if (path != null) {
                    System.out.println("    Detailed path:");
                    for (Stmt s : path) {
                        System.out.println("      -" + s + " => in method: " + icfg.getMethodOf(s));
                    }
                    if (pathContainCallToImplicitMechanism(Arrays.asList(path))) {
                        System.out.println("Leak through implicit call");
                    }
                }
            }
        } else {
            Writer.v().pwarning("No leak found");
        }
    }

    private boolean pathContainCallToImplicitMechanism(List<Stmt> path) {
        for (Stmt s : path) {
            SootMethod sm = icfg.getMethodOf(s);
            if (ExecuteesManager.v().isExecutee(sm)) {
                return true;
            }
        }
        return false;
    }
}