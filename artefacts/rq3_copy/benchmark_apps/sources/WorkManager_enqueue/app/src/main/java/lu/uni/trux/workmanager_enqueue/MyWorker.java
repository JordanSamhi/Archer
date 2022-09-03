package lu.uni.trux.workmanager_enqueue;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public static TelephonyManager tm;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String phoneNumber = tm.getLine1Number();
        Log.d("LEAK", phoneNumber);
        return null;
    }
}
