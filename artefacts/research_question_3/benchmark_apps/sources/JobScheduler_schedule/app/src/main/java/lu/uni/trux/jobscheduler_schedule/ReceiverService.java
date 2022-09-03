package lu.uni.trux.jobscheduler_schedule;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ReceiverService extends JobService {

    public static TelephonyManager tm;

    @Override
    public boolean onStartJob(JobParameters params) {
        String phoneNumber = tm.getLine1Number();
        Log.d("LEAK", phoneNumber);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


}