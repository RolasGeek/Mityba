package com.studies.service;

import com.studies.model.Recipe;
import com.studies.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        recipeRep.save(recipe);
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        recipeRep.delete(recipe);
    }

    @Override
    public List<Recipe> getRecipes() {
        return recipeRep.findAll();
    }
}
