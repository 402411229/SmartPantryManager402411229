package com.example.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox alertCheck;
    private Spinner unitSpinner;
    private SharedPreferences prefs;

    private String[] units = {"pieces", "g", "kg", "ml", "l", "cups"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        alertCheck = findViewById(R.id.alertCheck);
        unitSpinner = findViewById(R.id.unitSpinner);
        Button save = findViewById(R.id.saveButton);

        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));

        alertCheck.setChecked(prefs.getBoolean("expiry_alerts", true));
        String saved = prefs.getString("default_unit", "pieces");
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(saved)) {
                unitSpinner.setSelection(i);
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("expiry_alerts", alertCheck.isChecked());
                editor.putString("default_unit", unitSpinner.getSelectedItem().toString());
                editor.apply();
                Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
