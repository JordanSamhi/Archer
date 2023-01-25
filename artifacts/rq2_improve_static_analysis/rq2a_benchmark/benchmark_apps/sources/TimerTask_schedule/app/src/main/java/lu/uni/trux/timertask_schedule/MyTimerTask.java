package lu.uni.trux.timertask_schedule;

import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    public static TelephonyManager tm;

    @Override
    public void run() {
        String phoneNumber = tm.getLine1Number();
        Log.d("LEAK", phoneNumber);
    }
}
