package lu.uni.trux.completablefuture_runasync;

import android.telephony.TelephonyManager;
import android.util.Log;

public class MyRunnable implements Runnable{
    public static TelephonyManager tm;

    @Override
    public void run() {
        String phoneNumber = tm.getLine1Number();
        Log.d("LEAK", phoneNumber);
    }
}
