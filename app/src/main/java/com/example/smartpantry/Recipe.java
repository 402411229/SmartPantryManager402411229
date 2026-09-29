package com.example.smartpantry;

import java.util.ArrayList;

public class Recipe {

    private int id;
    private String name;
    private String steps;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public Recipe(int id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSteps() {
        return steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient i) {
        ingredients.add(i);
    }
}
