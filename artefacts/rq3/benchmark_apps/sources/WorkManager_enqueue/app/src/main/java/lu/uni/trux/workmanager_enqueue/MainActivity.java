package lu.uni.trux.workmanager_enqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyWorker.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();
        PeriodicWorkRequest wr = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(wr);
    }
}