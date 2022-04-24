package lu.uni.archer.dataflowproblem;

import soot.Unit;
import soot.Value;

import java.util.Set;

public interface Problem {
    String getProblemName();

    void solve();

    Set<?> getResults(Value v, Unit u);
}
