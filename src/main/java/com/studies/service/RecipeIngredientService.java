package com.studies.service;

import com.studies.model.Ingredient;
import com.studies.model.RecipeIngredient;

import java.util.List;

public interface RecipeIngredientService {
    public List<RecipeIngredient> findRecipeIngredientsByRecipeId(Long id);
    public List<RecipeIngredient> findRecipeIngredientsByIngredientId(Long id);
    public void saveRecipeIngredient (RecipeIngredient recipeIngredient);
    public void removeRecipeIngredient (RecipeIngredient recipeIngredient);
}
