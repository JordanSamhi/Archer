package lu.uni.archer.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.javatuples.Triplet;

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

/**
 * This class sets the different option for the application
 *
 * @author Jordan Samhi
 */
public class CommandLineOptions {

    private static final Triplet<String, String, String> APK = new Triplet<>("apk", "a", "Apk file");
    private static final Triplet<String, String, String> PLATFORMS = new Triplet<>("platforms", "p", "Platform file");
    private static final Triplet<String, String, String> HELP = new Triplet<>("help", "h", "Print this message");
    private static final Triplet<String, String, String> REDIS_SERVER = new Triplet<>("redis-srv", "s", "Sets the redis server address");
    private static final Triplet<String, String, String> REDIS_PORT = new Triplet<>("redis-port", "n", "Sets the redis port to connect to");
    private static final Triplet<String, String, String> REDIS_PWD = new Triplet<>("redis-pwd", "w", "Sets the redis password");
    private static final Triplet<String, String, String> CALL_GRAPH = new Triplet<>("callgraph", "c", "Sets the call graph algorithm (CHA, RTA, VTA, SPARK)");
    private static final Triplet<String, String, String> TIMEOUT = new Triplet<>("timeout", "to", "Sets the timeout for the analysis");
    private static final Triplet<String, String, String> TAINT_ANALYSIS = new Triplet<>("taint-analysis", "t", "Run taint analysis on the app");
    private static final Triplet<String, String, String> RAW = new Triplet<>("raw", "r", "Print raw results");
    private static final Triplet<String, String, String> OUTPUT = new Triplet<>("output", "o", "Write instrumented file in given folder");

    private final Options options;
    private final Options firstOptions;
    private final CommandLineParser parser;
    private CommandLine cmdLine;

    private static CommandLineOptions instance;

    public CommandLineOptions() {
        this.options = new Options();
        this.firstOptions = new Options();
        this.initOptions();
        this.parser = new DefaultParser();
    }

    public static CommandLineOptions v() {
        if (instance == null) {
            instance = new CommandLineOptions();
        }
        return instance;
    }

    public void parseArgs(String[] args) {
        this.parse(args);
    }

    /**
     * This method does the parsing of the arguments.
     * It distinguished, real options and help option.
     *
     * @param args the arguments of the application
     */
    private void parse(String[] args) {
        HelpFormatter formatter;
        try {
            CommandLine cmdFirstLine = this.parser.parse(this.firstOptions, args, true);
            if (cmdFirstLine.hasOption(HELP.getValue0())) {
                formatter = new HelpFormatter();
                formatter.printHelp(Constants.TOOL_NAME, this.options, true);
                System.exit(0);
            }
            this.cmdLine = this.parser.parse(this.options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Initialization of all recognized options
     */
    private void initOptions() {
        final Option apk = Option.builder(APK.getValue1())
                .longOpt(APK.getValue0())
                .desc(APK.getValue2())
                .hasArg(true)
                .argName(APK.getValue0())
                .required(true)
                .build();

        final Option output = Option.builder(OUTPUT.getValue1())
                .longOpt(OUTPUT.getValue0())
                .desc(OUTPUT.getValue2())
                .hasArg(true)
                .argName(OUTPUT.getValue0())
                .required(false)
                .build();

        final Option cg = Option.builder(CALL_GRAPH.getValue1())
                .longOpt(CALL_GRAPH.getValue0())
                .desc(CALL_GRAPH.getValue2())
                .hasArg(true)
                .argName(CALL_GRAPH.getValue0())
                .required(false)
                .build();

        final Option raw = Option.builder(RAW.getValue1())
                .longOpt(RAW.getValue0())
                .desc(RAW.getValue2())
                .hasArg(false)
                .argName(RAW.getValue0())
                .required(false)
                .build();

        final Option platform = Option.builder(PLATFORMS.getValue1())
                .longOpt(PLATFORMS.getValue0())
                .desc(PLATFORMS.getValue2())
                .hasArg(true)
                .argName(PLATFORMS.getValue0())
                .required(true)
                .build();

        final Option timeout = Option.builder(TIMEOUT.getValue1())
                .longOpt(TIMEOUT.getValue0())
                .desc(TIMEOUT.getValue2())
                .argName(TIMEOUT.getValue0())
                .hasArg(true)
                .build();

        final Option redisServer = Option.builder(REDIS_SERVER.getValue1())
                .longOpt(REDIS_SERVER.getValue0())
                .desc(REDIS_SERVER.getValue2())
                .hasArg(true)
                .argName(REDIS_SERVER.getValue0())
                .required(false)
                .build();

        final Option redisPort = Option.builder(REDIS_PORT.getValue1())
                .longOpt(REDIS_PORT.getValue0())
                .desc(REDIS_PORT.getValue2())
                .hasArg(true)
                .argName(REDIS_PORT.getValue0())
                .required(false)
                .build();

        final Option redisPwd = Option.builder(REDIS_PWD.getValue1())
                .longOpt(REDIS_PWD.getValue0())
                .desc(REDIS_PWD.getValue2())
                .hasArg(true)
                .argName(REDIS_PWD.getValue0())
                .required(false)
                .build();

        final Option help = Option.builder(HELP.getValue1())
                .longOpt(HELP.getValue0())
                .desc(HELP.getValue2())
                .argName(HELP.getValue0())
                .build();

        final Option taintAnalysis = Option.builder(TAINT_ANALYSIS.getValue1())
                .longOpt(TAINT_ANALYSIS.getValue0())
                .desc(TAINT_ANALYSIS.getValue2())
                .argName(TAINT_ANALYSIS.getValue0())
                .required(false)
                .hasArg(false)
                .build();

        this.firstOptions.addOption(help);

        this.options.addOption(apk);
        this.options.addOption(platform);
        this.options.addOption(redisPwd);
        this.options.addOption(raw);
        this.options.addOption(timeout);
        this.options.addOption(cg);
        this.options.addOption(output);
        this.options.addOption(taintAnalysis);
        this.options.addOption(redisPort);
        this.options.addOption(redisServer);

        for (Option o : this.firstOptions.getOptions()) {
            this.options.addOption(o);
        }
    }

    public String getApk() {
        return cmdLine.getOptionValue(APK.getValue0());
    }

    public String getPlatforms() {
        return cmdLine.getOptionValue(PLATFORMS.getValue0());
    }

    public boolean hasRedisServer() {
        return this.cmdLine.hasOption(REDIS_SERVER.getValue1());
    }

    public boolean hasRedisPwd() {
        return this.cmdLine.hasOption(REDIS_PWD.getValue1());
    }

    public boolean hasRedisPort() {
        return this.cmdLine.hasOption(REDIS_PORT.getValue1());
    }

    public boolean hasCallGraph() {
        return this.cmdLine.hasOption(CALL_GRAPH.getValue1());
    }

    public boolean hasTaintAnalysis() {
        return this.cmdLine.hasOption(TAINT_ANALYSIS.getValue1());
    }

    public boolean hasRedisEnv() {
        return this.hasRedisServer() && this.hasRedisPort() && this.hasRedisPwd();
    }

    public String getRedisServer() {
        return cmdLine.getOptionValue(REDIS_SERVER.getValue0());
    }

    public String getRedisPwd() {
        return cmdLine.getOptionValue(REDIS_PWD.getValue0());
    }

    public String getRedisPort() {
        return cmdLine.getOptionValue(REDIS_PORT.getValue0());
    }

    public String getCallGraph() {
        return cmdLine.getOptionValue(CALL_GRAPH.getValue0());
    }

    public boolean hasRaw() {
        return this.cmdLine.hasOption(RAW.getValue1());
    }

    public boolean hasOutput() {
        return this.cmdLine.hasOption(OUTPUT.getValue1());
    }

    public String getOutput() {
        return cmdLine.getOptionValue(OUTPUT.getValue0());
    }

    public boolean hasTimeout() {
        return this.cmdLine.hasOption(TIMEOUT.getValue1());
    }

    public int getTimeout() {
        int n;
        try {
            n = Integer.parseInt(cmdLine.getOptionValue(TIMEOUT.getValue0()));
        } catch (NumberFormatException ignored) {
            return 60;
        }
        return n;
    }
}
