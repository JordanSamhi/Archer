package lu.uni.trux.workmanager_enqueue1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyWorker.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();
        OneTimeWorkRequest wr = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(wr);
    }
}