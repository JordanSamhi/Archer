package lu.uni.trux.poolexecutor_schedulewithfixeddelay_enqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyWorker.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        MyRunnable.c = this;
        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
        try {
            stpe.scheduleWithFixedDelay(new MyRunnable(), 10, 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Something wrong happened");
            e.printStackTrace();
        }
    }
}
