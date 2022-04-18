package lu.uni.archer.analysis;

import heros.InterproceduralCFG;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;

public abstract class IFDSProblem<T> extends DefaultJimpleIFDSTabulationProblem<T, InterproceduralCFG<Unit, SootMethod>> {

    public IFDSProblem(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    public abstract String getAnalysisName();
}
