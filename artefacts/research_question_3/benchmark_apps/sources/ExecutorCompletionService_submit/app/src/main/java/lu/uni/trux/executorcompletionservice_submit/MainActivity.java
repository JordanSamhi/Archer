package lu.uni.trux.executorcompletionservice_submit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyCallable.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        ExecutorCompletionService ecs = new ExecutorCompletionService(Executors.newScheduledThreadPool(1));
        Future f = ecs.submit(new MyCallable());
    }
}