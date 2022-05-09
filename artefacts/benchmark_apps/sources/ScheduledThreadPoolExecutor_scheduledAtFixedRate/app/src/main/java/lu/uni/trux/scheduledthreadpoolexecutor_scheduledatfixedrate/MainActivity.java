package lu.uni.trux.scheduledthreadpoolexecutor_scheduledatfixedrate;

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
        MyRunnable.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
        stpe.scheduleAtFixedRate(new MyRunnable(), 1, 1, TimeUnit.SECONDS);
    }
}