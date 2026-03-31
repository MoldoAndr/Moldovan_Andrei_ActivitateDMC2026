package labs.mta.laborator4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_MARKET = 100;
    private static final int REQUEST_CODE_EDIT_MARKET = 101;

    private ArrayList<Market> listaMarketuri;
    private MarketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdaugaMarket = findViewById(R.id.btnAdaugaMarket);
        ListView listViewMarkets = findViewById(R.id.listViewMarkets);

        listaMarketuri = new ArrayList<>();
        incarcaMarketuriDinFisier();
        adapter = new MarketAdapter(this, listaMarketuri);

        listViewMarkets.setAdapter(adapter);

        listViewMarkets.setOnItemClickListener((parent, view, position, id) -> {
            Market marketSelectat = listaMarketuri.get(position);
            Intent intent = new Intent(MainActivity.this, AddMarketActivity.class);
            intent.putExtra(AddMarketActivity.EXTRA_MARKET, marketSelectat);
            intent.putExtra(AddMarketActivity.EXTRA_POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_EDIT_MARKET);
        });

        listViewMarkets.setOnItemLongClickListener((parent, view, position, id) -> {
            Market marketSelectat = listaMarketuri.get(position);
            salveazaFavorit(marketSelectat);
            Toast.makeText(MainActivity.this,
                    R.string.adaugat_la_favorite,
                    Toast.LENGTH_SHORT).show();
            return true;
        });


        Button btnSetari = findViewById(R.id.btnSetari);
        btnSetari.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnAdaugaMarket.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddMarketActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_MARKET);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        Market market = IntentCompat.getParcelableExtra(data, AddMarketActivity.EXTRA_MARKET, Market.class);
        if (market == null) {
            return;
        }

        if (requestCode == REQUEST_CODE_ADD_MARKET) {
            listaMarketuri.add(market);
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_EDIT_MARKET) {
            int position = data.getIntExtra(AddMarketActivity.EXTRA_POSITION, -1);
            if (position >= 0 && position < listaMarketuri.size()) {
                actualizeazaMarket(listaMarketuri.get(position), market);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void incarcaMarketuriDinFisier() {
        try (FileInputStream fis = openFileInput("markets.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Market m = Market.fromFileLine(line);
                    if (m != null) {
                        listaMarketuri.add(m);
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salveazaFavorit(Market market) {
        try (FileOutputStream fos = openFileOutput("favorite.txt", MODE_APPEND)) {
            String line = market.toFileLine() + "\n";
            fos.write(line.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizeazaMarket(Market marketInitial, Market marketActualizat) {
        marketInitial.setNume(marketActualizat.getNume());
        marketInitial.setNonStop(marketActualizat.isNonStop());
        marketInitial.setNrAngajati(marketActualizat.getNrAngajati());
        marketInitial.setTip(marketActualizat.getTip());
        marketInitial.setRating(marketActualizat.getRating());
        marketInitial.setAreParcare(marketActualizat.isAreParcare());
        marketInitial.setAreLivrare(marketActualizat.isAreLivrare());
        marketInitial.setZona(marketActualizat.getZona());
        marketInitial.setLocaltime(marketActualizat.getLocaltime());
    }
}
