package labs.mta.lab10;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText cityEditText;
    private Spinner forecastSpinner;
    private Button searchButton;
    private TextView resultTextView;
    private static final String API_KEY = "YOUR_API_KEY_HERE";

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

        cityEditText = findViewById(R.id.cityEditText);
        forecastSpinner = findViewById(R.id.forecastSpinner);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);

        String[] options = {"1 zi", "5 zile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forecastSpinner.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityEditText.getText().toString();
                if (!cityName.isEmpty()) {
                    String selectedOption = forecastSpinner.getSelectedItem().toString();
                    String days = "1day";
                    if (selectedOption.equals("5 zile")) days = "5day";

                    new FetchCityKeyTask(days).execute(cityName);
                }
            }
        });
    }

    private class FetchCityKeyTask extends AsyncTask<String, Void, String> {
        private String forecastDays;

        FetchCityKeyTask(String forecastDays) {
            this.forecastDays = forecastDays;
        }

        @Override
        protected String doInBackground(String... params) {
            String cityName = params[0];
            String result;
            try {
                String urlString = "https://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + API_KEY + "&q=" + cityName;
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONArray jsonArray = new JSONArray(response.toString());
                    if (jsonArray.length() > 0) {
                        JSONObject cityObject = jsonArray.getJSONObject(0);
                        result = cityObject.getString("Key");
                    } else {
                        result = "Orasul nu a fost gasit";
                    }
                } else {
                    result = "Eroare HTTP Oras: " + responseCode;
                }

            } catch (Exception e) {
                Log.e("FetchCityKeyTask", "Eroare la apelul API Oras", e);
                result = "Eroare: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith("Eroare") || result.equals("Orasul nu a fost gasit")) {
                resultTextView.setText(result);
            } else {
                new FetchWeatherTask(forecastDays).execute(result);
            }
        }
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        private String forecastDays;

        FetchWeatherTask(String forecastDays) {
            this.forecastDays = forecastDays;
        }

        @Override
        protected String doInBackground(String... params) {
            String locationKey = params[0];
            StringBuilder result = new StringBuilder();
            try {
                String urlString = "https://dataservice.accuweather.com/forecasts/v1/daily/" + forecastDays + "/" + locationKey + "?apikey=" + API_KEY + "&metric=true";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray dailyForecasts = jsonObject.getJSONArray("DailyForecasts");
                    
                    for (int i = 0; i < dailyForecasts.length(); i++) {
                        JSONObject forecast = dailyForecasts.getJSONObject(i);
                        String date = forecast.getString("Date").substring(0, 10);
                        JSONObject temperature = forecast.getJSONObject("Temperature");
                        
                        double minTemp = temperature.getJSONObject("Minimum").getDouble("Value");
                        double maxTemp = temperature.getJSONObject("Maximum").getDouble("Value");

                        result.append("Data: ").append(date).append("\n");
                        result.append("Min: ").append(minTemp).append("°C, Max: ").append(maxTemp).append("°C\n");
                        result.append("--------------------------\n");
                    }
                } else {
                    result.append("Eroare HTTP Vreme: ").append(responseCode);
                }
            } catch (Exception e) {
                Log.e("FetchWeatherTask", "Eroare la apelul API Vreme", e);
                result.append("Eroare Vreme: ").append(e.getMessage());
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            resultTextView.setText(result);
        }
    }
}
