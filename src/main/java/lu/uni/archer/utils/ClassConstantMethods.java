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
    List<String> methodsThatPropagateToBase0;
    List<String> methodsThatPropagateToBase1;
    List<String> methodsThatPropagateBaseToReceiver;
    private static ClassConstantMethods instance;

    public static ClassConstantMethods v() {
        if (instance == null) {
            instance = new ClassConstantMethods();
        }
        return instance;
    }

    private ClassConstantMethods() {
        this.methodsThatPropagateToBase0 = new ArrayList<>();
        this.methodsThatPropagateToBase1 = new ArrayList<>();
        this.methodsThatPropagateBaseToReceiver = new ArrayList<>();

        // parameter of interest is arg 0
        this.methodsThatPropagateToBase0.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToBase0.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,java.time.Duration)>");
        this.methodsThatPropagateToBase0.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,long,java.util.concurrent.TimeUnit,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateToBase0.add("<androidx.work.PeriodicWorkRequest$Builder: void <init>(java.lang.Class,java.time.Duration,java.time.Duration)>");
        this.methodsThatPropagateToBase0.add("<androidx.work.OneTimeWorkRequest$Builder: void <init>(java.lang.Class)>");

        // parameter of interest is arg 1
        this.methodsThatPropagateToBase1.add("<android.content.ComponentName: void <init>(android.content.Context,java.lang.Class)>");
        this.methodsThatPropagateToBase1.add("<android.app.job.JobInfo$Builder: void <init>(int,android.content.ComponentName)>");

        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.WorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");

        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.PeriodicWorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");

        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder getThis()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder keepResultsForAtLeast(java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setBackoffCriteria(androidx.work.BackoffPolicy,java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setConstraints(androidx.work.Constraints)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setExpedited(androidx.work.OutOfQuotaPolicy)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialDelay(java.time.Duration)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialRunAttemptCount(int)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInitialState(androidx.work.WorkInfo$State)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setInputData(androidx.work.Data)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setPeriodStartTime(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder setScheduleRequestedAt(long,java.util.concurrent.TimeUnit)>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest build()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest buildInternal()>");
        this.methodsThatPropagateBaseToReceiver.add("<androidx.work.OneTimeWorkRequest$Builder: androidx.work.WorkRequest$Builder addTag(java.lang.String)>");

        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setRequiredNetworkType(int)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setRequiresCharging(boolean)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setMinimumLatency(long)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo build()>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setExtras(android.os.PersistableBundle)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setRequiresDeviceIdle(boolean)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder addTriggerContentUri(android.app.job.JobInfo$TriggerContentUri)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setTriggerContentUpdateDelay(long)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setTriggerContentMaxDelay(long)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setPeriodic(long)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setPeriodic(long,long)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setOverrideDeadline(long)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setBackoffCriteria(long,int)>");
        this.methodsThatPropagateBaseToReceiver.add("<android.app.job.JobInfo$Builder: android.app.job.JobInfo$Builder setPersisted(boolean)>");
    }

    public boolean isInMethodsThatPropagateToBase(SootMethod sm) {
        return this.methodsThatPropagateToBase0.contains(sm.getSignature()) || this.methodsThatPropagateToBase1.contains(sm.getSignature());
    }

    public boolean isInMethodsThatPropagateBaseToReceiver(SootMethod sm) {
        return this.methodsThatPropagateBaseToReceiver.contains(sm.getSignature());
    }

    public Value getClassConstant(InvokeExpr ie) {
        SootMethod sm = ie.getMethod();
        if (this.isInMethodsThatPropagateToBase(sm)) {
            if (this.methodsThatPropagateToBase0.contains(sm.getSignature())) {
                return ie.getArg(0);
            } else if (this.methodsThatPropagateToBase1.contains(sm.getSignature())) {
                return ie.getArg(1);
            }
        }
        return null;
    }

}
