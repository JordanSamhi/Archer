package lu.uni.archer.utils;

import soot.SootMethod;
import soot.Value;
import soot.jimple.InvokeExpr;

import java.util.ArrayList;
import java.util.List;

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

public class ClassConstantMethods {
    List<String> classConstantMethods;
    List<String> propagationMethods;
    private static ClassConstantMethods instance;

    public static ClassConstantMethods v() {
        if (instance == null) {
            instance = new ClassConstantMethods();
        }
        return instance;
    }

    private ClassConstantMethods() {
        this.classConstantMethods = new ArrayList<>();
        this.propagationMethods = new ArrayList<>();
        this.classConstantMethods.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,long,java.util.concurrent.TimeUnit)>");
        this.classConstantMethods.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,java.time.Duration)>");
        this.classConstantMethods.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,long,java.util.concurrent.TimeUnit,long,java.util.concurrent.TimeUnit)>");
        this.classConstantMethods.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,java.time.Duration,java.time.Duration)>");
        this.classConstantMethods.add("<androidx.work.OneTimeWorkRequest$Builder: void <init>(java.lang.Class)>");

        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.propagationMethods.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");

        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.propagationMethods.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");

        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.propagationMethods.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");
    }

    public boolean containsMethodUsingClassConstant(SootMethod sm) {
        return this.classConstantMethods.contains(sm.getSignature());
    }

    public boolean containsPropagationMethods(SootMethod sm) {
        return this.propagationMethods.contains(sm.getSignature());
    }

    public Value getClassConstant(InvokeExpr ie) {
        SootMethod sm = ie.getMethod();
        if (this.containsMethodUsingClassConstant(sm)) {
            String className = sm.getDeclaringClass().getName();
            if (className.equals(Constants.PERIODIC_WORK_REQUEST) || className.equals(Constants.ONE_TIME_WORK_REQUEST) || className.equals(Constants.WORK_REQUEST)) {
                return ie.getArg(0);
            }
        }
        return null;
    }

}
