package com.studies.service;

import com.studies.model.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe findRecipeById(Long id);
    void saveRecipe(Recipe recipe);
    void updateRecipe(Recipe recipe);
    void removeRecipe(Recipe recipe);
    List<Recipe> getRecipes();
}
