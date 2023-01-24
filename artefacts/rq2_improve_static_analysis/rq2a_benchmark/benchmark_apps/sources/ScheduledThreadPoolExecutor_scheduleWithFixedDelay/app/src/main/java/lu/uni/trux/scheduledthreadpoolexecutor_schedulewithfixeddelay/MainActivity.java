package lu.uni.trux.scheduledthreadpoolexecutor_schedulewithfixeddelay;

import static lu.uni.trux.scheduledthreadpoolexecutor_schedulewithfixeddelay.MyRunnable.tm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tm = tm;

        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
        try {
            stpe.scheduleWithFixedDelay(new MyRunnable(), 1, -1, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Something wrong happened");
            e.printStackTrace();
        }
    }
}