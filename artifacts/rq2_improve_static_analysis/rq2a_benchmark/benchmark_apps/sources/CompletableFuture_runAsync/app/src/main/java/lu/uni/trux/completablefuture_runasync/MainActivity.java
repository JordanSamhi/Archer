package lu.uni.trux.completablefuture_runasync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyRunnable.tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        CompletableFuture.runAsync(new MyRunnable());
    }
}