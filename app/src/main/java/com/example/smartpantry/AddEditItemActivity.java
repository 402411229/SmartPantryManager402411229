package com.example.smartpantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText nameInput, quantityInput, expiryInput;
    private Spinner unitSpinner;
    private DatabaseHelper db;
    private int itemId = -1;

    private String[] units = {"pieces", "g", "kg", "ml", "l", "cups", "tbsp", "tsp", "slices"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        db = new DatabaseHelper(this);

        nameInput = findViewById(R.id.nameInput);
        quantityInput = findViewById(R.id.quantityInput);
        expiryInput = findViewById(R.id.expiryInput);
        unitSpinner = findViewById(R.id.unitSpinner);
        Button save = findViewById(R.id.saveButton);

        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units));

        // if an id came through the intent we are editing, otherwise we are adding
        itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1) {
            setTitle(R.string.add_item);
        } else {
            setTitle(R.string.edit_item);
            fillForm();
        }

        expiryInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void fillForm() {
        PantryItem item = db.getItem(itemId);
        if (item == null) {
            return;
        }
        nameInput.setText(item.getName());
        if (item.getQuantity() == (int) item.getQuantity()) {
            quantityInput.setText(String.valueOf((int) item.getQuantity()));
        } else {
            quantityInput.setText(String.valueOf(item.getQuantity()));
        }
        expiryInput.setText(item.getExpiry());

        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(item.getUnit())) {
                unitSpinner.setSelection(i);
            }
        }
    }

    private void pickDate() {
        Calendar today = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String monthPart = String.valueOf(month + 1);
                if (monthPart.length() == 1) {
                    monthPart = "0" + monthPart;
                }
                String dayPart = String.valueOf(day);
                if (dayPart.length() == 1) {
                    dayPart = "0" + dayPart;
                }
                expiryInput.setText(year + "-" + monthPart + "-" + dayPart);
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveItem() {
        String name = nameInput.getText().toString().trim();
        String quantityText = quantityInput.getText().toString().trim();

        if (name.isEmpty()) {
            nameInput.setError("Please enter the ingredient name");
            return;
        }
        if (quantityText.isEmpty()) {
            quantityInput.setError("Please enter a quantity");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            quantityInput.setError("Quantity must be a number");
            return;
        }

        if (quantity <= 0) {
            quantityInput.setError("Quantity must be more than zero");
            return;
        }

        String unit = unitSpinner.getSelectedItem().toString();
        String expiry = expiryInput.getText().toString().trim();

        if (itemId == -1) {
            db.addItem(name, quantity, unit, expiry);
            Toast.makeText(this, name + " added to your pantry", Toast.LENGTH_SHORT).show();
        } else {
            db.updateItem(itemId, name, quantity, unit, expiry);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
