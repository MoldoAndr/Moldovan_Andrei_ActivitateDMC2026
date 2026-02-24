package atm.labs.lab2;

import android.os.Bundle;
import android.widget.TextView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.etName);
        Button btn = findViewById(R.id.btnAction);

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