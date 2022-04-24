package lu.uni.archer.dataflowproblem;

import heros.InterproceduralCFG;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
import soot.jimple.toolkits.ide.JimpleIFDSSolver;

public abstract class IFDSProblem<T> extends DefaultJimpleIFDSTabulationProblem<T, InterproceduralCFG<Unit, SootMethod>> implements Problem {

    protected JimpleIFDSSolver<?, InterproceduralCFG<Unit, SootMethod>> solver;

    public IFDSProblem(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
        this.solver = new JimpleIFDSSolver<>(this);
    }

    @Override
    public void solve() {
        this.solver.solve();
    }
}
