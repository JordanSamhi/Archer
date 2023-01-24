package lu.uni.trux.synchronousexecutor_execute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyRunnable.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Object cl = Class.forName("androidx.work.impl.utils.SynchronousExecutor").newInstance();
            Method m = cl.getClass().getDeclaredMethod("execute", Runnable.class);
            m.invoke(cl, new MyRunnable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}