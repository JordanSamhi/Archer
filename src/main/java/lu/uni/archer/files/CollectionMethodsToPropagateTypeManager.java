package lu.uni.archer.files;

import lu.uni.archer.utils.Constants;
import soot.SootMethod;

import java.util.HashMap;
import java.util.Map;

public class CollectionMethodsToPropagateTypeManager extends FileLoader {

    private static CollectionMethodsToPropagateTypeManager instance;

    private final Map<String, Integer> methodToArgPosition;

    private CollectionMethodsToPropagateTypeManager() {
        super();
        this.methodToArgPosition = new HashMap<>();
        this.loadMethods();
    }

    public static CollectionMethodsToPropagateTypeManager v() {
        if (instance == null) {
            instance = new CollectionMethodsToPropagateTypeManager();
        }
        return instance;
    }

    protected void loadMethods() {
        for (String line : this.items) {
            String[] split = line.split("\\|");
            if (split.length < 2) {
                continue;
            }
            String method = split[0];
            int argPosition = Integer.parseInt(split[1]);
            methodToArgPosition.put(method, argPosition);
        }
    }

    @Override
    protected String getFile() {
        return Constants.COLLECTION_METHODS;
    }

    public boolean isCollectionMethodToPropagateType(SootMethod sm) {
        return this.methodToArgPosition.containsKey(sm.getSignature());
    }

    public int getArgPosition(SootMethod sm) {
        if (this.isCollectionMethodToPropagateType(sm)) {
            return this.methodToArgPosition.get(sm.getSignature());
        }
        return -1;
    }
}
