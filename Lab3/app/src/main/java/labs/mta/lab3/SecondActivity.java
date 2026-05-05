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

// Laborator 3 - Cerinta 5: SecondActivity lansata la deschiderea aplicatiei (setata LAUNCHER in AndroidManifest)
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private static final int REQ_THIRD = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.i(TAG, "onCreate()");

        Button btn = findViewById(R.id.button);
        // Laborator 3 - Cerinta 7: La apasarea butonului se deschide ThirdActivity printr-un Intent
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);

            // Laborator 3 - Cerinta 8: Trimitere mesaj si doua valori int catre ThirdActivity prin Bundle
            Bundle b = new Bundle();
            b.putString("msg", "Salut din SecondActivity");
            b.putInt("x", 7);
            b.putInt("y", 5);

            intent.putExtras(b);
            startActivityForResult(intent, REQ_THIRD);
        });
    }

    // Laborator 3 - Cerinta 2: Implementare metode ale ciclului de viata
    // Laborator 3 - Cerinta 3: Log-uri de tipuri diferite (Log.e/w/d/i/v)
    // Laborator 3 - Cerinta 4: Filtrate in Logcat dupa TAG="SecondActivity"
    @Override
    protected void onStart() {
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

    // Laborator 3 - Cerinta 11: Afisare mesaj primit si suma calculata printr-un Toast
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_THIRD && resultCode == RESULT_OK && data != null) {
            String backMsg = data.getStringExtra("back_msg");
            int sum = data.getIntExtra("sum", 0);

            Toast.makeText(this, backMsg + "    | suma=" + sum, Toast.LENGTH_LONG).show();
        }
    }
}