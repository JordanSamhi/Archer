package lu.uni.trux.completablefuture_thenrun;

import android.util.Log;

public class MyRunnable implements Runnable{
    public static String phoneNumber;

    @Override
    public void run() {
        Log.d("LEAK", phoneNumber);
    }
}
