package lu.uni.archer.files;

import lu.uni.archer.utils.Constants;
import soot.SootMethod;

public class MethodsRemoveCallGraphManager extends FileLoader {

    private static MethodsRemoveCallGraphManager instance;

    private MethodsRemoveCallGraphManager() {
        super();
    }

    public static MethodsRemoveCallGraphManager v() {
        if (instance == null) {
            instance = new MethodsRemoveCallGraphManager();
        }
        return instance;
    }

    @Override
    protected String getFile() {
        return Constants.TO_REMOVE_CG_METHODS;
    }

    public boolean isInMethodsToRemove(SootMethod sm) {
        return this.items.contains(sm.getSubSignature());
    }
}
