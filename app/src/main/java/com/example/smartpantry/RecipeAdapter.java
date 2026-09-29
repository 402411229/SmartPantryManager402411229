package com.example.smartpantry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private ArrayList<Recipe> recipes;
    private Context context;
    private ArrayList<PantryItem> pantry;
    private boolean almostThere = false;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    // used by the almost there list so the row can say what is still needed
    public RecipeAdapter(Context context, ArrayList<Recipe> recipes, ArrayList<PantryItem> pantry) {
        this.context = context;
        this.recipes = recipes;
        this.pantry = pantry;
        this.almostThere = true;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.getName());
        if (almostThere) {
            holder.info.setText("You still need " + RecipeMatcher.getMissingName(recipe, pantry));
        } else {
            holder.info.setText(recipe.getIngredients().size() + " ingredients you already have");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("recipe_id", recipe.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {

        TextView name, info;

        RecipeHolder(View view) {
            super(view);
            name = view.findViewById(R.id.recipeName);
            info = view.findViewById(R.id.recipeInfo);
        }
    }
}
