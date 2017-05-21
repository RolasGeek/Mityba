package com.studies.service;

import com.studies.model.Recipe;
import com.studies.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("recipeService")
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRep;

    @Override
    public Recipe findRecipeById(Long id) {
        return recipeRep.findById(id);
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        recipe.setCreationDate(Calendar.getInstance().getTime());
        recipe.setFavouriteCount(0);
        recipe.setUnseenDays(0);
        recipe.setViewCount(0);
        recipeRep.save(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe){recipeRep.save(recipe);}

    @Override
    public void removeRecipe(Recipe recipe) {
        recipeRep.delete(recipe);
    }

    @Override
    public List<Recipe> getRecipes() {
        return recipeRep.findAll();
    }
}
