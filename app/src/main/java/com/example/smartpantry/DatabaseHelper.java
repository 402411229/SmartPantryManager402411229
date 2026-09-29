package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT, " +
                "expiry TEXT)");

        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "steps TEXT)");

        db.execSQL("CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, " +
                "name TEXT, " +
                "quantity REAL, " +
                "unit TEXT, " +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id))");

        loadRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }

    // the 18 recipes that get put in the database the first time the app runs
    private void loadRecipes(SQLiteDatabase db) {
        String[][] data = {
            {"Scrambled Eggs", "Beat the eggs with the milk and salt. Melt the butter in a pan on medium heat. Pour in the eggs and stir slowly until they are soft and set.",
                "eggs:2:pieces|milk:2:tbsp|salt:1:tsp|butter:1:tbsp"},
            {"Cheese Toastie", "Butter the outside of both slices of bread. Put the cheese in the middle. Fry in a pan until both sides are golden brown.",
                "bread:2:slices|cheese:2:slices|butter:1:tbsp"},
            {"Tomato Pasta", "Boil the pasta until soft and drain it. Fry the chopped onion in the oil, add the chopped tomatoes and salt and cook for 10 minutes. Mix the sauce through the pasta.",
                "pasta:200:g|tomatoes:3:pieces|onions:1:pieces|oil:2:tbsp|salt:1:tsp"},
            {"Fried Rice", "Heat the oil and fry the chopped onion. Push it to the side and scramble the eggs in the same pan. Add the cooked rice and fry everything together for 5 minutes.",
                "rice:2:cups|eggs:2:pieces|onions:1:pieces|oil:2:tbsp"},
            {"Chicken Stew", "Brown the chicken in the oil. Add the chopped onion and tomatoes and the salt. Cover and simmer for 35 minutes until the chicken is tender.",
                "chicken:500:g|onions:1:pieces|tomatoes:2:pieces|oil:2:tbsp|salt:1:tsp"},
            {"Mince and Rice", "Fry the chopped onion, add the mince and break it up while it browns. Season with salt and simmer for 20 minutes. Serve on the cooked rice.",
                "mince:500:g|rice:2:cups|onions:1:pieces|salt:1:tsp"},
            {"Mashed Potatoes", "Peel and boil the potatoes until soft. Drain them and mash with the butter, milk and salt until smooth.",
                "potatoes:4:pieces|butter:2:tbsp|milk:100:ml|salt:1:tsp"},
            {"Pap", "Boil 3 cups of water with the salt. Stir in the maize meal, cover and steam on low heat for 30 minutes, stirring now and then.",
                "maize meal:2:cups|salt:1:tsp"},
            {"Peanut Butter Sandwich", "Spread the peanut butter on one slice of bread and close it with the other slice.",
                "bread:2:slices|peanut butter:2:tbsp"},
            {"Banana Oats", "Cook the oats in the milk on low heat for 5 minutes. Slice the banana on top before serving.",
                "oats:1:cups|milk:1:cups|bananas:1:pieces"},
            {"Pancakes", "Mix the flour, egg, milk and sugar into a smooth batter. Fry thin pancakes in a little oil until light brown on both sides.",
                "flour:1:cups|milk:1:cups|eggs:1:pieces|sugar:2:tbsp|oil:1:tbsp"},
            {"Spinach and Potato", "Boil the diced potatoes until nearly soft. Fry the onion in the oil, add the chopped spinach and the potatoes and cook together for 10 minutes.",
                "spinach:1:cups|potatoes:2:pieces|onions:1:pieces|oil:2:tbsp"},
            {"Bean Stew", "Fry the onion in the oil until soft. Add the tomatoes and the beans and simmer for 20 minutes.",
                "beans:2:cups|tomatoes:2:pieces|onions:1:pieces|oil:2:tbsp"},
            {"Garlic Bread", "Mix the crushed garlic into the butter. Spread it on the bread and toast until crispy.",
                "bread:4:slices|butter:2:tbsp|garlic:2:pieces"},
            {"Cheese Omelette", "Beat the eggs with the salt. Pour into a buttered pan, add the cheese and fold the omelette in half once it sets.",
                "eggs:3:pieces|cheese:1:slices|salt:1:tsp|butter:1:tbsp"},
            {"Tomato Soup", "Fry the onion in the oil, add the chopped tomatoes, salt and 2 cups of water. Simmer for 20 minutes and blend or mash it smooth.",
                "tomatoes:5:pieces|onions:1:pieces|salt:1:tsp|oil:1:tbsp"},
            {"Yoghurt Banana Bowl", "Spoon the yoghurt into a bowl, slice the banana over it and sprinkle the oats on top.",
                "yoghurt:1:cups|bananas:1:pieces|oats:0.5:cups"},
            {"Chicken Fried Rice", "Fry the chopped chicken in the oil until cooked. Add the onion, then the eggs, then the cooked rice and fry everything together.",
                "chicken:300:g|rice:2:cups|eggs:2:pieces|onions:1:pieces|oil:2:tbsp"}
        };

        for (String[] row : data) {
            ContentValues values = new ContentValues();
            values.put("name", row[0]);
            values.put("steps", row[1]);
            long recipeId = db.insert("recipes", null, values);

            String[] parts = row[2].split("\\|");
            for (String part : parts) {
                String[] bits = part.split(":");
                ContentValues ing = new ContentValues();
                ing.put("recipe_id", recipeId);
                ing.put("name", bits[0]);
                ing.put("quantity", Double.parseDouble(bits[1]));
                ing.put("unit", bits[2]);
                db.insert("recipe_ingredients", null, ing);
            }
        }
    }

    public long addItem(String name, double quantity, String unit, String expiry) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry", expiry);
        return getWritableDatabase().insert("pantry", null, values);
    }

    public void updateItem(int id, String name, double quantity, String unit, String expiry) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry", expiry);
        getWritableDatabase().update("pantry", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteItem(int id) {
        getWritableDatabase().delete("pantry", "id = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<PantryItem> getPantry() {
        ArrayList<PantryItem> items = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM pantry ORDER BY name", null);
        while (c.moveToNext()) {
            items.add(new PantryItem(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("quantity")),
                    c.getString(c.getColumnIndexOrThrow("unit")),
                    c.getString(c.getColumnIndexOrThrow("expiry"))));
        }
        c.close();
        return items;
    }

    public PantryItem getItem(int id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM pantry WHERE id = ?",
                new String[]{String.valueOf(id)});
        PantryItem item = null;
        if (c.moveToFirst()) {
            item = new PantryItem(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getDouble(c.getColumnIndexOrThrow("quantity")),
                    c.getString(c.getColumnIndexOrThrow("unit")),
                    c.getString(c.getColumnIndexOrThrow("expiry")));
        }
        c.close();
        return item;
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM recipes ORDER BY name", null);
        while (c.moveToNext()) {
            Recipe r = new Recipe(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("steps")));
            recipes.add(r);
        }
        c.close();

        for (Recipe r : recipes) {
            Cursor ic = db.rawQuery("SELECT * FROM recipe_ingredients WHERE recipe_id = ?",
                    new String[]{String.valueOf(r.getId())});
            while (ic.moveToNext()) {
                r.addIngredient(new Ingredient(
                        ic.getString(ic.getColumnIndexOrThrow("name")),
                        ic.getDouble(ic.getColumnIndexOrThrow("quantity")),
                        ic.getString(ic.getColumnIndexOrThrow("unit"))));
            }
            ic.close();
        }
        return recipes;
    }

    public Recipe getRecipe(int id) {
        for (Recipe r : getAllRecipes()) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
}
