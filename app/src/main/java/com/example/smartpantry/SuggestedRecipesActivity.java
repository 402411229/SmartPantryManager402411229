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
    private RecyclerView list, almostList;
    private TextView emptyText, infoText, almostTitle, almostInfo;
    private View divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle(R.string.suggested_recipes);

        db = new DatabaseHelper(this);
        list = findViewById(R.id.recipeList);
        almostList = findViewById(R.id.almostList);
        emptyText = findViewById(R.id.emptyText);
        infoText = findViewById(R.id.infoText);
        almostTitle = findViewById(R.id.almostTitle);
        almostInfo = findViewById(R.id.almostInfo);
        divider = findViewById(R.id.divider);
        list.setLayoutManager(new LinearLayoutManager(this));
        almostList.setLayoutManager(new LinearLayoutManager(this));
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
        ArrayList<Recipe> almost = RecipeMatcher.getAlmostThere(all, pantry);

        list.setAdapter(new RecipeAdapter(this, matches));
        infoText.setText(matches.size() + " of " + all.size() + " recipes can be made with what you have");

        if (matches.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }

        // the almost there list is only shown when there is something in it
        almostList.setAdapter(new RecipeAdapter(this, almost, pantry));
        if (almost.isEmpty()) {
            divider.setVisibility(View.GONE);
            almostTitle.setVisibility(View.GONE);
            almostInfo.setVisibility(View.GONE);
            almostList.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
            almostTitle.setVisibility(View.VISIBLE);
            almostInfo.setVisibility(View.VISIBLE);
            almostList.setVisibility(View.VISIBLE);
        }
    }
}
