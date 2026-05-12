package labs.mta.lab11;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivitateCharts extends AppCompatActivity {
    private static final String CHART_TYPE_PIE = "PieChart";
    private static final String CHART_TYPE_COLUMN = "ColumnChart";
    private static final String CHART_TYPE_BAR = "BarChart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            float[] values = bundle.getFloatArray("values");
            String type = bundle.getString("chartType", CHART_TYPE_PIE);

            if (CHART_TYPE_COLUMN.equals(type)) {
                setContentView(R.layout.column_chart);
                applyInsets();

                if (values != null) {
                    ColumnChart columnChart = findViewById(R.id.columnChart);
                    columnChart.setData(values);
                }
            } else if (CHART_TYPE_BAR.equals(type)) {
                setContentView(R.layout.bar_chart);
                applyInsets();

                if (values != null) {
                    BarChart barChart = findViewById(R.id.barChart);
                    barChart.setData(values);
                }
            } else {
                setContentView(R.layout.piechart);
                applyInsets();

                if (values != null) {
                    PieChart pieChart = findViewById(R.id.pieChart);
                    pieChart.setData(values);
                }
            }
        } else {
            setContentView(R.layout.piechart);
            applyInsets();
        }
    }

    private void applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
