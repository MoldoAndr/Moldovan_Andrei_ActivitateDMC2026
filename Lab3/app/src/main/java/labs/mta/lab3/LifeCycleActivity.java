package labs.mta.lab3;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

// Laborator 3 - Cerintele 1, 5: Activitate noua adaugata in proiect
public class LifeCycleActivity extends AppCompatActivity {

    private static final String TAG = "LC_LifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);

        // Laborator 3 - Cerinta 3: Log de tip info in onCreate
        Log.i(TAG, "onCreate()");
    }

    // Laborator 3 - Cerinta 2: Implementare metoda ciclului de viata onStart()
    @Override
    protected void onStart() {
        super.onStart();
        logAll("onStart()");
    }

    // Laborator 3 - Cerinta 2: Implementare metoda ciclului de viata onResume()
    @Override
    protected void onResume() {
        super.onResume();
        logAll("onResume()");
    }

    // Laborator 3 - Cerinta 2: Implementare metoda ciclului de viata onPause()
    @Override
    protected void onPause() {
        logAll("onPause()");
        super.onPause();
    }

    // Laborator 3 - Cerinta 2: Implementare metoda ciclului de viata onStop()
    @Override
    protected void onStop() {
        logAll("onStop()");
        super.onStop();
    }

    // Laborator 3 - Cerinta 3: Salvare log-uri de tip error, warning, debug, info, verbose
    // Laborator 3 - Cerinta 4: Aceste log-uri pot fi filtrate in tab-ul Logcat din Android Studio dupa TAG="LC_LifeCycle"
    private void logAll(String method) {
        Log.e(TAG, method + " -> Log.e");
        Log.w(TAG, method + " -> Log.w");
        Log.d(TAG, method + " -> Log.d");
        Log.i(TAG, method + " -> Log.i");
        Log.v(TAG, method + " -> Log.v");
    }
}