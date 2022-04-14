package lu.uni.archer.config;

import lu.uni.archer.utils.CommandLineOptions;
import soot.Scene;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.options.Options;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SootConfig implements IInfoflowConfig {

    public void setSootOptions(Options options, InfoflowConfiguration config) {
        Options.v().set_process_multiple_dex(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_none);
        Options.v().set_whole_program(true);
        Options.v().setPhaseOption("cg", "enabled:true");
        config.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
    }
}
