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
    List<String> methodsThatPropagateToBase;
    List<String> methodsThatPropagateToReceiver;
    private static ClassConstantMethods instance;

    public static ClassConstantMethods v() {
        if (instance == null) {
            instance = new ClassConstantMethods();
        }
        return instance;
    }

    private ClassConstantMethods() {
        this.methodsThatPropagateToBase = new ArrayList<>();
        this.methodsThatPropagateToReceiver = new ArrayList<>();
        this.methodsThatPropagateToBase.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToBase.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,java.time.Duration)>");
        this.methodsThatPropagateToBase.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,long,java.util.concurrent.TimeUnit,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToBase.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,java.time.Duration,java.time.Duration)>");
        this.methodsThatPropagateToBase.add("<androidx.work.OneTimeWorkRequest$Builder: void <init>(java.lang.Class)>");
        this.methodsThatPropagateToBase.add("<android.content.ComponentName: void <init>(android.content.Context,java.lang.Class)>");

        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");

        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");

        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.methodsThatPropagateToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");
    }

    public boolean isInMethodsThatPropagateToBase(SootMethod sm) {
        return this.methodsThatPropagateToBase.contains(sm.getSignature());
    }

    public boolean isInMethodsThatPropagateToReceiver(SootMethod sm) {
        return this.methodsThatPropagateToReceiver.contains(sm.getSignature());
    }

    public Value getClassConstant(InvokeExpr ie) {
        SootMethod sm = ie.getMethod();
        if (this.isInMethodsThatPropagateToBase(sm)) {
            String className = sm.getDeclaringClass().getName();
            if (className.equals(Constants.PERIODIC_WORK_REQUEST) || className.equals(Constants.ONE_TIME_WORK_REQUEST) || className.equals(Constants.WORK_REQUEST)) {
                return ie.getArg(0);
            } else if (className.equals(Constants.COMPONENT_NAME)) {
                return ie.getArg(1);
            }
        }
        return null;
    }

}
