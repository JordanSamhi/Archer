package lu.uni.archer.files;

import soot.SootClass;
import soot.SootMethod;

public class ExecuteesManager extends FileLoader {

    private static ExecuteesManager instance;

    private ExecuteesManager() {
        super();
    }

    public static ExecuteesManager v() {
        if (instance == null) {
            instance = new ExecuteesManager();
        }
        return instance;
    }

    @Override
    protected String getFile() {
        return "/executees.txt";
    }

    public boolean isExecutee(SootMethod sm) {
        for (String s : this.items) {
            if (sm.getSignature().equals(s)) {
                return true;
            }
        }
        return false;
    }
}