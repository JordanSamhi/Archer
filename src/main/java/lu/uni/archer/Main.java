package lu.uni.archer;

import lu.uni.archer.analysis.*;
import lu.uni.archer.config.SootConfig;
import lu.uni.archer.utils.CommandLineOptions;
import lu.uni.archer.utils.Constants;
import lu.uni.archer.utils.Writer;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.profiler.StopWatch;
import soot.*;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.android.manifest.ProcessManifest;

import java.util.Date;

/*-
 * #%L
 * Archer
 *
 * %%
 * Copyright (C) 2022 Jordan Samhi
 * University of Luxembourg - Interdisciplinary Centre for
 * Security Reliability and Trust (SnT) - TruX - All rights reserved
 *
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

public class Main {
    public static void main(String[] args) {
        StopWatch analysisTime = new StopWatch("Analysis");
        analysisTime.start("Analysis");
        CommandLineOptions.v().parseArgs(args);
        Writer.v().pinfo(String.format("%s v1.0 started on %s\n", Constants.TOOL_NAME, new Date()));

        ProcessManifest pm = null;
        try {
            pm = new ProcessManifest(CommandLineOptions.v().getApk());
        } catch (Exception ignored) {
        }
        if (pm != null) {
            Writer.v().pinfo(String.format("Processing: %s", pm.getPackageName()));
        }

        Writer.v().pinfo("Initializing Environment");
        SetupApplication sa = initializeSoot();
        Writer.v().psuccess("Done");

        if (CommandLineOptions.v().hasRedisEnv()) {
            Writer.v().pinfo("Executing empirical study");
            EmpiricalStudy es = new EmpiricalStudy(CommandLineOptions.v().getRedisServer(), CommandLineOptions.v().getRedisPort(), CommandLineOptions.v().getRedisPwd());
            es.run();
            Writer.v().psuccess("End of empirical study");
        }

        StopWatch instrumentationTime = new StopWatch("Instrumentation");
        instrumentationTime.start("Instrumentation");
        Writer.v().pinfo("Solving data flow problems...");
        Analyses.v().addProblem(new IFDSFieldPropagation());
        Analyses.v().addProblem(new IFDSClassConstantPropagation());
        Analyses.v().addProblem(new IFDSMethodsCalledPropagation());
        Analyses.v().addProblem(new IFDSPossibleTypes());
        Analyses.v().solveProblems();
        Writer.v().psuccess("Done");


        Writer.v().pinfo("Patching Call graph...");
        Instrumenter.v().instrument();
        instrumentationTime.stop();
        ResultsAccumulator.v().setInstrumentationElapsedTime(instrumentationTime.elapsedTime() / 1000000000);

        sa.constructCallgraph();
        CallGraphPatcher.v().patch();
        Writer.v().psuccess("Done");

        if (CommandLineOptions.v().hasTaintAnalysis()) {
            StopWatch taintAnalysisTime = new StopWatch("Taint Analysis");
            taintAnalysisTime.start("Taint Analysis");
            Writer.v().pinfo("Running taint analysis");
            FlowAnalysis fa = new FlowAnalysis(sa);
            fa.runTaintAnalysis();
            Writer.v().psuccess("Taint analysis terminated");
            fa.printResults();
            taintAnalysisTime.stop();
            ResultsAccumulator.v().setTaintAnalysisElapsedTime(taintAnalysisTime.elapsedTime() / 1000000000);
        }

        analysisTime.stop();
        ResultsAccumulator.v().setAppName(FilenameUtils.getBaseName(CommandLineOptions.v().getApk()));
        ResultsAccumulator.v().setAnalysisElapsedTime(analysisTime.elapsedTime() / 1000000000);
        if (CommandLineOptions.v().hasRaw()) {
            ResultsAccumulator.v().printVectorResults();
        } else {
            ResultsAccumulator.v().printResults();
        }
    }

    /**
     * Initialize Soot options
     */
    private static SetupApplication initializeSoot() {
        G.reset();
        InfoflowAndroidConfiguration ifac = new InfoflowAndroidConfiguration();
        ifac.getAnalysisFileConfig().setAndroidPlatformDir(CommandLineOptions.v().getPlatforms());
        ifac.getAnalysisFileConfig().setTargetAPKFile(CommandLineOptions.v().getApk());
        ifac.setMergeDexFiles(true);
        SetupApplication sa = new SetupApplication(ifac);
        sa.setSootConfig(new SootConfig());
        sa.getConfig().setIgnoreFlowsInSystemPackages(false);
        sa.constructCallgraph();
        sa.getConfig().setSootIntegrationMode(InfoflowAndroidConfiguration.SootIntegrationMode.UseExistingInstance);
        return sa;
    }
}