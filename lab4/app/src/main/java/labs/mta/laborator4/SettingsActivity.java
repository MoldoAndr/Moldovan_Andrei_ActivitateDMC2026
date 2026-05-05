package labs.mta.laborator4;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Laborator 7 - Cerinta 4: Activitate de setari - utilizatorul isi seteaza dimensiunea si culoarea textelor
public class SettingsActivity extends AppCompatActivity {

    private EditText etTextSize;
    private Spinner spinnerColor;
    private final String[] colorNames = {"Negru", "Rosu", "Albastru", "Verde"};
    private final int[] colorValues = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etTextSize = findViewById(R.id.etTextSize);
        spinnerColor = findViewById(R.id.spinnerColor);
        Button btnSalveazaSetari = findViewById(R.id.btnSalveazaSetari);

        ArrayAdapter<String> adapterColor = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                colorNames
        );
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapterColor);

        incarcaSetari();

        btnSalveazaSetari.setOnClickListener(view -> salveazaSetari());
    }

    // Laborator 7 - Cerinta 4: Citire setari anterioare din SharedPreferences pentru afisare in formularul de setari
    private void incarcaSetari() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        float textSize = prefs.getFloat("text_size", 16f);
        int textColor = prefs.getInt("text_color", Color.BLACK);

        etTextSize.setText(String.valueOf((int) textSize));

        for (int i = 0; i < colorValues.length; i++) {
            if (colorValues[i] == textColor) {
                spinnerColor.setSelection(i);
                break;
            }
        }
    }

    // Laborator 7 - Cerinta 4: Salvare setari (dimensiune text + culoare text) in SharedPreferences "settings"
    // Aceste setari sunt folosite in AddMarketActivity la viitoarele utilizari ale aplicatiei
    private void salveazaSetari() {
        String sizeText = etTextSize.getText().toString().trim();
        if (sizeText.isEmpty()) {
            etTextSize.setError("Introduceti dimensiunea");
            return;
        }

        float textSize;
        try {
            textSize = Float.parseFloat(sizeText);
        } catch (NumberFormatException e) {
            etTextSize.setError("Numar invalid");
            return;
        }

        int colorIndex = spinnerColor.getSelectedItemPosition();
        int textColor = colorValues[colorIndex];

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("text_size", textSize);
        editor.putInt("text_color", textColor);
        editor.apply();

        Toast.makeText(this, "Setari salvate", Toast.LENGTH_SHORT).show();
        finish();
    }
}
