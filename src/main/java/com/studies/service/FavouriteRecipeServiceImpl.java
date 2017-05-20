package com.studies.service;

import com.studies.model.FavouriteRecipe;
import com.studies.model.Ingredient;
import com.studies.repository.FavouriteRecipeRepository;
import com.studies.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("favouriteRecipeService")
public class FavouriteRecipeServiceImpl implements FavouriteRecipeService {
    @Autowired
    private FavouriteRecipeRepository favouriteRecipeRep;


    @Override
    public List<FavouriteRecipe> findFavouriteRecipeByUsername(String username) {
        return favouriteRecipeRep.findAllByUsername(username);
    }

    @Override
    public List<FavouriteRecipe> findFavouriteRecipeByRecipeId(Long id) {
        return favouriteRecipeRep.findAllByRecipeId(id);
    }

    @Override
    public void saveFavouriteRecipe(FavouriteRecipe favouriteRecipe) {
        favouriteRecipeRep.save(favouriteRecipe);
    }

    @Override
    public void removeFavouriteRecipe(FavouriteRecipe favouriteRecipe) {
        favouriteRecipeRep.delete(favouriteRecipe);
    }

    @Override
    public List<FavouriteRecipe> getFavouriteRecipes() {
        return favouriteRecipeRep.findAll();
    }
}
