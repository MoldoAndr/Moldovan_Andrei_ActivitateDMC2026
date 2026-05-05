package atm.labs.lab2;

import android.os.Bundle;
import android.widget.TextView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Laborator 2 - Cerinta 1: Aplicatie Android Studio cu activitate Empty Views, in Java, cu API 24
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Laborator 2 - Cerinta 2: Aplicatia este rulata pe emulator sau dispozitiv mobil
        setContentView(R.layout.activity_main);

        // Laborator 2 - Cerintele 5, 6, 7: Referinte catre cele 3 view-uri definite in layout cu constrangeri si texte din resurse
        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.etName);
        Button btn = findViewById(R.id.btnAction);

        // Laborator 2 - Cerinta 8 (Optional): La apasarea butonului, textul din TextView se modifica cu alt text
        btn.setOnClickListener(v -> {
            String input = et.getText().toString().trim();

            if (TextUtils.isEmpty(input)) {
                tv.setText(R.string.tv_changed);
            } else {
                tv.setText(input);
            }
        });
    }
}