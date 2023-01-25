package lu.uni.trux.executorcompletionservice_submit;

import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.concurrent.Callable;

public class MyCallable implements Callable {
    public static TelephonyManager tm;

    @Override
    public Object call() throws Exception {
        String phoneNumber = tm.getLine1Number();
        Log.d("LEAK", phoneNumber);
        return 1;
    }
}
