package lu.uni.trux.timertask_schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyTimerTask.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Timer t = new Timer();
        t.schedule(new MyTimerTask(), 10000);
    }
}