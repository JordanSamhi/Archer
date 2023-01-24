package lu.uni.trux.poolexecutor_schedulewithfixeddelay_enqueue;

import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MyRunnable implements Runnable{
    public static Context c;

    @Override
    public void run() {
        OneTimeWorkRequest wr = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(c).enqueue(wr);
    }
}
