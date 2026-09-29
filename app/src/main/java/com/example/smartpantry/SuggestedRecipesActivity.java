package com.example.smartpantry;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView list;
    private TextView emptyText, infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle(R.string.suggested_recipes);

        db = new DatabaseHelper(this);
        list = findViewById(R.id.recipeList);
        emptyText = findViewById(R.id.emptyText);
        infoText = findViewById(R.id.infoText);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRecipes();
    }

    private void showRecipes() {
        ArrayList<PantryItem> pantry = db.getPantry();
        ArrayList<Recipe> all = db.getAllRecipes();
        ArrayList<Recipe> matches = RecipeMatcher.getSuggestions(all, pantry);

        list.setAdapter(new RecipeAdapter(this, matches));
        infoText.setText(matches.size() + " of " + all.size() + " recipes can be made with what you have");

        if (matches.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }
}
