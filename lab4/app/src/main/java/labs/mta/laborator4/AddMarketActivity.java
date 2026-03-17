package labs.mta.laborator4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.time.LocalTime;

public class AddMarketActivity extends AppCompatActivity {

    private EditText etNume;
    private EditText etNrAngajati;
    private TimePicker etTime;
    private CheckBox cbNonStop;
    private RadioButton rbUrban;
    private RadioButton rbRural;
    private Spinner spinnerTip;
    private Spinner spinnerRating;
    private Switch switchParcare;
    private ToggleButton toggleLivrare;
    private Button btnSalveaza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_market);
        etTime = findViewById(R.id.timePicker);
        etNume = findViewById(R.id.etNume);
        etNrAngajati = findViewById(R.id.etNrAngajati);
        cbNonStop = findViewById(R.id.cbNonStop);
        rbUrban = findViewById(R.id.rbUrban);
        rbRural = findViewById(R.id.rbRural);
        spinnerTip = findViewById(R.id.spinnerTip);
        spinnerRating = findViewById(R.id.spinnerRating);
        switchParcare = findViewById(R.id.switchParcare);
        toggleLivrare = findViewById(R.id.toggleLivrare);
        btnSalveaza = findViewById(R.id.btnSalveaza);

        LocalTime currentTime = LocalTime.now();
        etTime.setIs24HourView(true);
        etTime.setHour(currentTime.getHour());
        etTime.setMinute(currentTime.getMinute());

        ArrayAdapter<TipMarket> adapterTip = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TipMarket.values()
        );
        adapterTip.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTip.setAdapter(adapterTip);

        String[] ratings = {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapterRating = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ratings
        );
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRating.setAdapter(adapterRating);

        btnSalveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salveazaMarket();
            }
        });
    }

    private void salveazaMarket() {
        String nume = etNume.getText().toString().trim();
        String nrAngajatiText = etNrAngajati.getText().toString().trim();
        LocalTime time = LocalTime.of(etTime.getHour(), etTime.getMinute());
        if (nume.isEmpty()) {
            etNume.setError("Introduceți numele marketului");
            return;
        }

        if (nrAngajatiText.isEmpty()) {
            etNrAngajati.setError("Introduceți numărul de angajați");
            return;
        }

        int nrAngajati;
        try {
            nrAngajati = Integer.parseInt(nrAngajatiText);
        } catch (NumberFormatException e) {
            etNrAngajati.setError("Numar invalid");
            return;
        }

        boolean nonStop = cbNonStop.isChecked();
        TipMarket tip = (TipMarket) spinnerTip.getSelectedItem();
        float rating = Float.parseFloat(spinnerRating.getSelectedItem().toString());
        boolean areParcare = switchParcare.isChecked();
        boolean areLivrare = toggleLivrare.isChecked();

        String zona;
        if (rbUrban.isChecked()) {
            zona = "Urban";
        } else if (rbRural.isChecked()) {
            zona = "Rural";
        } else {
            Toast.makeText(this, "Selectati zona", Toast.LENGTH_SHORT).show();
            return;
        }

        Market market = new Market(
                nume,
                nonStop,
                nrAngajati,
                tip,
                rating,
                areParcare,
                areLivrare,
                zona,
                time
        );

        Intent rezultat = new Intent();
        rezultat.putExtra("market", market);
        setResult(RESULT_OK, rezultat);
        finish();
    }
}
