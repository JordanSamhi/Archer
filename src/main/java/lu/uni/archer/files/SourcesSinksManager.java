package lu.uni.archer.files;

import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Utils;
import soot.SootMethod;
import soot.jimple.infoflow.android.data.AndroidMethod;

import java.util.HashSet;
import java.util.Set;

public class SourcesSinksManager extends FileLoader {

	private static SourcesSinksManager instance;
	private final Set<AndroidMethod> sources;
	private final Set<AndroidMethod> sinks;

	private SourcesSinksManager() {
		super();
		this.sources = new HashSet<>();
		this.sinks = new HashSet<>();
		this.loadSourcesSinks();
	}

	public static SourcesSinksManager v() {
		if(instance == null) {
			instance = new SourcesSinksManager();
		}
		return instance;
	}

	private void loadSourcesSinks() {
		String[] split;
		String type, signature;
		for(String method: this.items) {
			split = method.split("\\|");
			if(split.length == 2) {
				type = split[0];
				signature = split[1];
				AndroidMethod am = new AndroidMethod(Utils.getMethodNameFromSignature(signature),
						Utils.getParametersNamesFromSignature(signature),
						Utils.getReturnNameFromSignature(signature),
						Utils.getClassNameFromSignature(signature));
				if(type.equals(Constants.SOURCE)) {
					this.sources.add(am);
				}else if(type.equals(Constants.SINK)) {
					this.sinks.add(am);
				}
			}
		}
	}

	public Set<AndroidMethod> getSources() {
		return this.sources;
	}

	public Set<AndroidMethod> getSinks() {
		return this.sinks;
	}

	public void addSink(SootMethod sm) {
		this.sinks.add(new AndroidMethod(sm));
	}
	
	public void addSource(SootMethod sm) {
		this.sources.add(new AndroidMethod(sm));
	}

	@Override
	protected String getFile() {
		return Constants.SOURCES_SINKS_FILE;
	}
}