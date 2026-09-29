package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setTitle(R.string.recipe_detail);

        int id = getIntent().getIntExtra("recipe_id", -1);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipe(id);

        TextView title = findViewById(R.id.titleText);
        TextView ingredients = findViewById(R.id.ingredientsText);
        TextView steps = findViewById(R.id.stepsText);

        if (recipe == null) {
            title.setText("Recipe not found");
            return;
        }

        title.setText(recipe.getName());

        String lines = "";
        for (Ingredient i : recipe.getIngredients()) {
            lines = lines + "- " + i.asText() + "\n";
        }
        ingredients.setText(lines.trim());
        steps.setText(recipe.getSteps());
    }
}
