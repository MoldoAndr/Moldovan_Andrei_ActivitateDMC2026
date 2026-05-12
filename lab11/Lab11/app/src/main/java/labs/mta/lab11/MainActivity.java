package labs.mta.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Float> values = new ArrayList<>();
    private final String[] spTypes = {"PieChart", "ColumnChart", "BarChart"};
    private EditText etValue;
    private TextView tvValues;

    private Spinner spTipChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etValue = findViewById(R.id.etValue);
        tvValues = findViewById(R.id.tvValues);
        spTipChart = findViewById(R.id.Spinner);
        ArrayAdapter<String> adapterTipChart = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spTypes
        );
        adapterTipChart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipChart.setAdapter(adapterTipChart);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnShowChart = findViewById(R.id.btnShowChart);
        Button btnClear = findViewById(R.id.btnClear);

        btnAdd.setOnClickListener(v -> {
            if (values.size() >= 10) {
                Toast.makeText(this, "Maximum 10 values allowed", Toast.LENGTH_SHORT).show();
                return;
            }

            String input = etValue.getText().toString();
            if (!input.isEmpty()) {
                try {
                    float val = Float.parseFloat(input);
                    values.add(val);
                    updateValuesText();
                    etValue.setText("");
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowChart.setOnClickListener(v -> {
            if (values.isEmpty()) {
                Toast.makeText(this, "Add at least one value", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ActivitateCharts.class);
            float[] valuesArray = new float[values.size()];
            for (int i = 0; i < values.size(); i++) {
                valuesArray[i] = values.get(i);
            }
            Bundle bundle = new Bundle();
            bundle.putFloatArray("values", valuesArray);
            bundle.putString("chartType", spTipChart.getSelectedItem().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnClear.setOnClickListener(v -> {
            values.clear();
            updateValuesText();
            etValue.setText("");
        });
    }

    private void updateValuesText() {
        StringBuilder sb = new StringBuilder("Values: ");
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i < values.size() - 1) {
                sb.append(", ");
            }
        }
        tvValues.setText(sb.toString());
    }
}
