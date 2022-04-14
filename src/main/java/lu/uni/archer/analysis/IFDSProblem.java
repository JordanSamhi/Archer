package lu.uni.archer.analysis;

import heros.InterproceduralCFG;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.ClassConstant;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
import soot.toolkits.scalar.Pair;

public abstract class IFDSProblem extends DefaultJimpleIFDSTabulationProblem<Pair<Value, ClassConstant>, InterproceduralCFG<Unit, SootMethod>> {

    public IFDSProblem(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    public abstract String getAnalysisName();
}
