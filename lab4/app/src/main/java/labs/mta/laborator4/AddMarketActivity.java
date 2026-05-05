package labs.mta.laborator4;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.content.IntentCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;

// Laborator 4 - Cerinta 3: Activitate pentru preluarea datelor unui Market (folosita si pentru editare - Lab 6)
public class AddMarketActivity extends AppCompatActivity {

    public static final String EXTRA_MARKET = "market";
    public static final String EXTRA_POSITION = "position";

    // Laborator 4 - Cerinta 3: View-uri pentru preluarea datelor - TextView, EditText, CheckBox, RadioButton, Spinner, Switch, ToggleButton, TimePicker
    // Nota: RatingBar inlocuit cu Spinner pentru selectia rating-ului (1-5)
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
    private Market marketPentruEditare;
    private int pozitieEditare = -1;

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
        etTime.setIs24HourView(true);

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
        // Laborator 6 - Cerinta 3: Pre-completare campuri cu datele obiectului selectat (daca este editare)
        incarcaDatePentruEditare();
        // Laborator 7 - Cerinta 4: Aplica setarile salvate din SharedPreferences (dimensiune si culoare text)
        aplicaSetariText();

        btnSalveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salveazaMarket();
            }
        });
    }

    // Laborator 4 - Cerintele 4, 5: Creare instanta Market din datele introduse de utilizator
    // Laborator 5 - Cerinta 3: Instanta creata este returnata catre MainActivity prin Intent cu Bundle/Parcelable
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

        Market market = marketPentruEditare == null ? new Market() : marketPentruEditare;
        market.setNume(nume);
        market.setNonStop(nonStop);
        market.setNrAngajati(nrAngajati);
        market.setTip(tip);
        market.setRating(rating);
        market.setAreParcare(areParcare);
        market.setAreLivrare(areLivrare);
        market.setZona(zona);
        market.setLocaltime(time);

        if (marketPentruEditare == null) {
            // Laborator 7 - Cerinta 2: La adaugare obiect nou, acesta este salvat in fisierul markets.txt
            salveazaInFisier(market);
        }

        // Laborator 4 - Cerinta 4 / Laborator 6 - Cerinta 4: Obiectul este trimis inapoi prin Parcelable
        Intent rezultat = new Intent();
        rezultat.putExtra(EXTRA_MARKET, market);
        rezultat.putExtra(EXTRA_POSITION, pozitieEditare);
        setResult(RESULT_OK, rezultat);
        finish();
    }

    // Laborator 7 - Cerinta 2: Scriere obiect nou in fisierul markets.txt in modul APPEND (adaugare la sfarsit)
    private void salveazaInFisier(Market market) {
        try (FileOutputStream fos = openFileOutput("markets.txt", MODE_APPEND)) {
            String line = market.toFileLine() + "\n";
            fos.write(line.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Laborator 7 - Cerinta 4: Citire setari din SharedPreferences si aplicare dimensiune + culoare la label-urile TextView
    private void aplicaSetariText() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        float textSize = prefs.getFloat("text_size", 16f);
        int textColor = prefs.getInt("text_color", Color.BLACK);

        TextView[] labels = {
                findViewById(R.id.tvFormTitle),
                findViewById(R.id.tvNumeLabel),
                findViewById(R.id.tvNrAngajatiLabel),
                findViewById(R.id.tvZonaLabel),
                findViewById(R.id.tvTipLabel),
                findViewById(R.id.tvRatingLabel),
                findViewById(R.id.tvOraLabel)
        };

        for (TextView tv : labels) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            tv.setTextColor(textColor);
        }
    }

    // Laborator 6 - Cerinta 3: Incarcare date din obiectul primit la editare si pre-completare a tuturor campurilor formularului
    private void incarcaDatePentruEditare() {
        pozitieEditare = getIntent().getIntExtra(EXTRA_POSITION, -1);
        marketPentruEditare = IntentCompat.getParcelableExtra(getIntent(), EXTRA_MARKET, Market.class);

        if (marketPentruEditare == null) {
            LocalTime currentTime = LocalTime.now();
            etTime.setHour(currentTime.getHour());
            etTime.setMinute(currentTime.getMinute());
            return;
        }

        etNume.setText(marketPentruEditare.getNume());
        etNrAngajati.setText(String.valueOf(marketPentruEditare.getNrAngajati()));
        cbNonStop.setChecked(marketPentruEditare.isNonStop());
        spinnerTip.setSelection(marketPentruEditare.getTip().ordinal());

        int ratingIndex = Math.max(0, Math.min(4, Math.round(marketPentruEditare.getRating()) - 1));
        spinnerRating.setSelection(ratingIndex);

        switchParcare.setChecked(marketPentruEditare.isAreParcare());
        toggleLivrare.setChecked(marketPentruEditare.isAreLivrare());

        if ("Urban".equalsIgnoreCase(marketPentruEditare.getZona())) {
            rbUrban.setChecked(true);
        } else if ("Rural".equalsIgnoreCase(marketPentruEditare.getZona())) {
            rbRural.setChecked(true);
        }

        LocalTime time = marketPentruEditare.getLocaltime();
        if (time != null) {
            etTime.setHour(time.getHour());
            etTime.setMinute(time.getMinute());
        }
    }
}
