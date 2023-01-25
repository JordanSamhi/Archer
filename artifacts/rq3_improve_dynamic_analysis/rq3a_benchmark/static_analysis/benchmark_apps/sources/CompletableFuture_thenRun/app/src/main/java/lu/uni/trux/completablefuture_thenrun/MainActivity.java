package lu.uni.trux.completablefuture_thenrun;

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
        MyRunnable.phoneNumber = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();

        CompletableFuture cf2 = new CompletableFuture();
        cf2 = CompletableFuture.runAsync(new MyRunnable1());
        cf2.thenRun(new MyRunnable());
    }
}