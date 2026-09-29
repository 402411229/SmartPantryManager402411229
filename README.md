# Smart Pantry Manager

An Android app written in Java that keeps track of the ingredients you already
have at home and then shows you only the recipes you can cook right now with
those ingredients. The idea is to cut food waste, so the app never suggests a
meal that needs a trip to the shop first.

Module: Mobile App Development 700
Student number: 402411229

## What the app does

* Add, edit and delete the ingredients in your pantry (name, quantity, unit and
  an optional expiry date)
* See the whole pantry in a list
* Open the Suggested Recipes screen to see only the meals you can make
* Tap a recipe to see the full ingredient list and the method
* Change your preferences on the Settings screen
* 18 recipes are loaded into the database the first time the app is opened

## Screens

| Screen | What it is for |
|---|---|
| MainActivity | the pantry list, with a button to add a new item |
| AddEditItemActivity | the form used to add a new item or edit an old one |
| SuggestedRecipesActivity | the recipes that match the pantry |
| RecipeDetailActivity | ingredients and method for one recipe |
| SettingsActivity | expiry warning and preferred unit |

## The matching rule

A recipe is only shown when **every** ingredient it needs is already in the
pantry. If a recipe needs 5 things and only 4 of them are in the pantry, that
recipe is left out completely, it is not shown as "almost there".

The comparing is done in `RecipeMatcher.java`. Before two names are compared
they are put in lower case and the plural is taken off, so "Tomatoes" in the
pantry still matches "tomato" in a recipe. Units are also cleaned up so that
"tablespoon" and "tbsp" count as the same unit. When the units are the same the
app also checks that there is enough of the ingredient, not just that it is
there.

## Database

The app uses **SQLite** through `SQLiteOpenHelper`.

I chose SQLite because:

* the app is only used by one person on one phone, so there is no need to share
  the data over the internet
* it works with no internet connection at all
* it does not need an account or a server to be set up, which keeps the app
  simple and free to run
* it is the way persistent storage is taught in the module

There are three tables:

* `pantry` - the ingredients the user has (id, name, quantity, unit, expiry)
* `recipes` - the recipe name and the method
* `recipe_ingredients` - the ingredients each recipe needs, linked to `recipes`
  by `recipe_id`

All four CRUD operations are used: adding an item, reading the list, updating an
item and deleting an item. The data stays on the phone after the app is closed
because it is written into the database and not kept in memory.

## How to run it

1. Open the project folder in Android Studio.
2. Let Gradle finish syncing.
3. Start an emulator (Pixel 3a, API 34 was used) or plug in a phone with USB
   debugging switched on.
4. Press Run.

Minimum SDK is 24 and the project was built with compile SDK 34.

## Screenshots

The `screenshots` folder has pictures of every screen, including the validation
error, the delete confirmation and the suggestions list before and after a new
ingredient is added.
