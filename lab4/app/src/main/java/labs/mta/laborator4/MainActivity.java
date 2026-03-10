package labs.mta.laborator4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_MARKET = 100;

    private Button btnAdaugaMarket;
    private ListView listViewMarkets;

    private ArrayList<Market> listaMarketuri;
    private ArrayList<String> listaAfisare;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdaugaMarket = findViewById(R.id.btnAdaugaMarket);
        listViewMarkets = findViewById(R.id.listViewMarkets);

        listaMarketuri = new ArrayList<>();
        listaAfisare = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaAfisare
        );

        listViewMarkets.setAdapter(adapter);

        btnAdaugaMarket.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddMarketActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_MARKET);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_MARKET && resultCode == RESULT_OK && data != null) {
            Market market = (Market) data.getSerializableExtra("market");

            if (market != null) {
                listaMarketuri.add(market);
                listaAfisare.add(formatMarket(market));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private String formatMarket(Market market) {
        return    "Nume: " + market.getNume() +
                "\nTip: " + market.getTip() +
                "\nAngajati: " + market.getNrAngajati() +
                "\nNon-stop: " + (market.isNonStop() ? "Da" : "Nu") +
                "\nRating: " + market.getRating() +
                "\nParcare: " + (market.isAreParcare() ? "Da" : "Nu") +
                "\nLivrare: " + (market.isAreLivrare() ? "Da" : "Nu") +
                "\nZona: " + market.getZona();
    }
}