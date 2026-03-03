package labs.mta.lab3;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    private int x = 0;
    private int y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Log.i(TAG, "onCreate()");

        Bundle extras = getIntent().getExtras();
        String msg = "default";

        if (extras != null) {
            msg = extras.getString("msg", "default");
            x = extras.getInt("x", 0);
            y = extras.getInt("y", 0);
        }
        Toast.makeText(this, msg + " | x=" + x + " y=" + y, Toast.LENGTH_LONG).show();
        Button btnBack = findViewById(R.id.button_back);
        btnBack.setOnClickListener(v -> {
            int sum = x + y;

            Intent back = new Intent();
            back.putExtra("back_msg", "Inapoi din ThirdActivity!");
            back.putExtra("sum", sum);

            setResult(RESULT_OK, back);
            finish();
        });
    }

    @Override protected void onStart()  { super.onStart();  logAll("onStart()"); }
    @Override protected void onResume() { super.onResume(); logAll("onResume()"); }
    @Override protected void onPause()  { logAll("onPause()"); super.onPause(); }
    @Override protected void onStop()   { logAll("onStop()");  super.onStop();  }

    private void logAll(String method) {
        Log.e(TAG, method + " -> Log.e");
        Log.w(TAG, method + " -> Log.w");
        Log.d(TAG, method + " -> Log.d");
        Log.i(TAG, method + " -> Log.i");
        Log.v(TAG, method + " -> Log.v");
    }
}