package lu.uni.trux.scheduledthreadpoolexecutor_invokeall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyCallable.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
        List<Callable<String>> callables = new ArrayList<>();
        callables.add(new MyCallable());
        try {
            stpe.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}