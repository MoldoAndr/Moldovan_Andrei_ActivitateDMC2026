package labs.mta.lab3;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

public class LifeCycleActivity extends AppCompatActivity {

    private static final String TAG = "LC_LifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);

        Log.i(TAG, "onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logAll("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logAll("onResume()");
    }

    @Override
    protected void onPause() {
        logAll("onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        logAll("onStop()");
        super.onStop();
    }

    private void logAll(String method) {
        Log.e(TAG, method + " -> Log.e");
        Log.w(TAG, method + " -> Log.w");
        Log.d(TAG, method + " -> Log.d");
        Log.i(TAG, method + " -> Log.i");
        Log.v(TAG, method + " -> Log.v");
    }
}