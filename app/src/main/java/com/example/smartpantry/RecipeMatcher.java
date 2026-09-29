package com.example.smartpantry;

import java.util.ArrayList;

// This class holds the main rule of the app: a recipe is only suggested when
// every ingredient it needs is already in the pantry in enough quantity.
public class RecipeMatcher {

    // makes words like "Tomatoes" and "tomato" come out the same so they can be compared
    public static String clean(String text) {
        if (text == null) {
            return "";
        }
        String s = text.toLowerCase().trim();
        if (s.endsWith("ies") && s.length() > 4) {
            s = s.substring(0, s.length() - 3) + "y";
        } else if (s.endsWith("es") && s.length() > 4) {
            String stem = s.substring(0, s.length() - 2);
            if (stem.endsWith("o") || stem.endsWith("s") || stem.endsWith("x")
                    || stem.endsWith("ch") || stem.endsWith("sh")) {
                s = stem;
            } else {
                s = s.substring(0, s.length() - 1);
            }
        } else if (s.endsWith("s") && s.length() > 3) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String cleanUnit(String unit) {
        String u = clean(unit);
        if (u.equals("gram") || u.equals("gm")) {
            u = "g";
        }
        if (u.equals("millilitre") || u.equals("milliliter")) {
            u = "ml";
        }
        if (u.equals("tablespoon")) {
            u = "tbsp";
        }
        if (u.equals("teaspoon")) {
            u = "tsp";
        }
        if (u.equals("")) {
            u = "piece";
        }
        return u;
    }

    // true when the pantry has this ingredient and enough of it
    public static boolean hasEnough(ArrayList<PantryItem> pantry, Ingredient needed) {
        for (PantryItem item : pantry) {
            if (clean(item.getName()).equals(clean(needed.getName()))) {
                String unitInPantry = cleanUnit(item.getUnit());
                String unitNeeded = cleanUnit(needed.getUnit());

                if (unitInPantry.equals(unitNeeded)) {
                    return item.getQuantity() >= needed.getQuantity();
                }
                // different units, we cannot convert them so we accept that the item is there
                return true;
            }
        }
        return false;
    }

    public static boolean canMake(Recipe recipe, ArrayList<PantryItem> pantry) {
        for (Ingredient needed : recipe.getIngredients()) {
            if (!hasEnough(pantry, needed)) {
                return false;
            }
        }
        return true;
    }

    // how many of the ingredients are not in the pantry
    public static int countMissing(Recipe recipe, ArrayList<PantryItem> pantry) {
        int missing = 0;
        for (Ingredient needed : recipe.getIngredients()) {
            if (!hasEnough(pantry, needed)) {
                missing = missing + 1;
            }
        }
        return missing;
    }

    // the name of the one ingredient that is still needed
    public static String getMissingName(Recipe recipe, ArrayList<PantryItem> pantry) {
        for (Ingredient needed : recipe.getIngredients()) {
            if (!hasEnough(pantry, needed)) {
                return needed.getName();
            }
        }
        return "";
    }

    // recipes that need only one more ingredient, these are kept separate from
    // the real suggestions because the user cannot cook them yet
    public static ArrayList<Recipe> getAlmostThere(ArrayList<Recipe> recipes, ArrayList<PantryItem> pantry) {
        ArrayList<Recipe> almost = new ArrayList<>();
        for (Recipe r : recipes) {
            if (countMissing(r, pantry) == 1) {
                almost.add(r);
            }
        }
        return almost;
    }

    public static ArrayList<Recipe> getSuggestions(ArrayList<Recipe> recipes, ArrayList<PantryItem> pantry) {
        ArrayList<Recipe> suggestions = new ArrayList<>();
        for (Recipe r : recipes) {
            if (canMake(r, pantry)) {
                suggestions.add(r);
            }
        }
        return suggestions;
    }
}
