package labs.mta.laborator4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_MARKET = 100;

    private ArrayList<Market> listaMarketuri;
    private ArrayList<String> listaAfisare;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdaugaMarket = findViewById(R.id.btnAdaugaMarket);
        ListView listViewMarkets = findViewById(R.id.listViewMarkets);

        listaMarketuri = new ArrayList<>();
        listaAfisare = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaAfisare
        );

        listViewMarkets.setAdapter(adapter);

        listViewMarkets.setOnItemClickListener((parent, view, position, id) -> {
            Market marketSelectat = listaMarketuri.get(position);
            Toast.makeText(MainActivity.this,
                    "Market selectat: " + marketSelectat.getNume(),
                    Toast.LENGTH_LONG).show();
        });

        listViewMarkets.setOnItemLongClickListener((parent, view, position, id) -> {
            listaMarketuri.remove(position);
            listaAfisare.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,
                    "Market sters",
                    Toast.LENGTH_SHORT).show();
            return true;
        });


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
                listaAfisare.add(market.toString());
                adapter.notifyDataSetChanged();
            }
        }
    }
}