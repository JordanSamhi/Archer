package lu.uni.trux.executorservice_submit;

import android.util.Log;

import java.util.concurrent.Callable;

public class MyCallable implements Callable {
    public static String phoneNumber;

    @Override
    public Object call() throws Exception {
        Log.d("LEAK", phoneNumber);
        return null;
    }
}
