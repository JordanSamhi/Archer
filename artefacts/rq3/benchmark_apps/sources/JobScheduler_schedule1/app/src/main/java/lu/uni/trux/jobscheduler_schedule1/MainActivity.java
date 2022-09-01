package lu.uni.trux.jobscheduler_schedule1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReceiverService.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        ComponentName cn = new ComponentName(getApplicationContext(), ReceiverService.class);
        JobInfo.Builder builder = new JobInfo.Builder(44, cn)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(true);
        builder.setMinimumLatency(1000);
        JobScheduler js = getSystemService(JobScheduler.class);
    }
}
