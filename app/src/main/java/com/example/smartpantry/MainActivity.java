package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView list;
    private TextView emptyText, countText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.my_pantry);

        db = new DatabaseHelper(this);

        list = findViewById(R.id.pantryList);
        emptyText = findViewById(R.id.emptyText);
        countText = findViewById(R.id.countText);
        list.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddEditItemActivity.class));
            }
        });
    }

    // the list is loaded again every time we come back to this screen
    @Override
    protected void onResume() {
        super.onResume();
        showPantry();
    }

    private void showPantry() {
        ArrayList<PantryItem> items = db.getPantry();
        list.setAdapter(new PantryAdapter(this, items));

        countText.setText(items.size() + " items in your pantry");

        if (items.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    public void editItem(int id) {
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra("item_id", id);
        startActivity(intent);
    }

    public void deleteItem(final PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete item")
                .setMessage("Remove " + item.getName() + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteItem(item.getId());
                    showPantry();
                    Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_suggested) {
            startActivity(new Intent(this, SuggestedRecipesActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
