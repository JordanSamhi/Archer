package lu.uni.trux.scheduledthreadpoolexecutor_invokeall;

import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {
    public static TelephonyManager tm;
    @Override
    public String call() throws Exception {
        String phoneNumber = tm.getLine1Number();
        Log.d("LEAK", phoneNumber);
        return null;
    }
}
