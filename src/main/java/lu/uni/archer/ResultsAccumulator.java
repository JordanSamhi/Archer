package lu.uni.archer;

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

public class ResultsAccumulator {

    private static ResultsAccumulator instance;

    private String appName;
    private long analysisElapsedTime;
    private long taintAnalysisElapsedTime;
    private long instrumentationElapsedTime;
    private int newEdgesInCG;

    private ResultsAccumulator() {
        this.setAppName("");
        this.setAnalysisElapsedTime(0);
        this.setInstrumentationElapsedTime(0);
        this.setTaintAnalysisElapsedTime(0);
        this.setNewEdgesInCG(0);
    }

    public static ResultsAccumulator v() {
        if (instance == null) {
            instance = new ResultsAccumulator();
        }
        return instance;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void printVectorResults() {
        System.out.println(this.generateVector());
    }

    private String generateVector() {
        return String.format("%s,%d,%d,%d,%d", this.getAppName(), this.getAnalysisElapsedTime(),
                this.getInstrumentationElapsedTime(), this.getTaintAnalysisElapsedTime(),
                this.newEdgesInCG);
    }

    public void printResults() {
        System.out.println("Results:");
        System.out.printf(" - App name: %s%n", this.getAppName());
        System.out.printf(" - Analysis elapsed time: %d%n", this.getAnalysisElapsedTime());
        System.out.printf(" - Instrumentation elapsed time: %d%n", this.getInstrumentationElapsedTime());
        System.out.printf(" - Taint Analysis elapsed time: %d%n", this.getTaintAnalysisElapsedTime());
        System.out.printf(" - Number of new Edges in call graph: %d%n", this.newEdgesInCG);
    }

    public long getAnalysisElapsedTime() {
        return analysisElapsedTime;
    }

    public void setAnalysisElapsedTime(long l) {
        this.analysisElapsedTime = l;
    }

    public long getTaintAnalysisElapsedTime() {
        return taintAnalysisElapsedTime;
    }

    public void setTaintAnalysisElapsedTime(long taintAnalysisElapsedTime) {
        this.taintAnalysisElapsedTime = taintAnalysisElapsedTime;
    }

    public long getInstrumentationElapsedTime() {
        return instrumentationElapsedTime;
    }

    public void setInstrumentationElapsedTime(long instrumentationElapsedTime) {
        this.instrumentationElapsedTime = instrumentationElapsedTime;
    }

    public void incrementNumberOfNewEdge() {
        this.newEdgesInCG++;
    }

    public void setNewEdgesInCG(int newEdgesInCG) {
        this.newEdgesInCG = newEdgesInCG;
    }
}