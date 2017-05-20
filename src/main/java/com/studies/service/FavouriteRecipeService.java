package com.studies.service;

import com.studies.model.FavouriteRecipe;
import com.studies.model.Ingredient;

import java.util.List;

public interface FavouriteRecipeService {
    List<FavouriteRecipe> findFavouriteRecipeByUsername(String username);
    List<FavouriteRecipe> findFavouriteRecipeByRecipeId(Long id);
    void saveFavouriteRecipe(FavouriteRecipe favouriteRecipe);
    void removeFavouriteRecipe(FavouriteRecipe favouriteRecipe);
    List<FavouriteRecipe> getFavouriteRecipes();
}
