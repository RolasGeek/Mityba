package com.studies.service;

import com.studies.model.RecipeIngredient;
import com.studies.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RecipeIngredientService")
public class RecipeIngredientServiceImpl implements RecipeIngredientService {
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public List<RecipeIngredient> findRecipeIngredientsByRecipeId(Long id) {
        return recipeIngredientRepository.findAllByRecipeId(id);}

    @Override
    public List<RecipeIngredient> findRecipeIngredientsByIngredientId(Long id) {
        return recipeIngredientRepository.findAllByIngredientId(id);
    }

    @Override
    public void saveRecipeIngredient (RecipeIngredient recipeIngredient) {recipeIngredientRepository.save(recipeIngredient);}

    @Override
    public void removeRecipeIngredient (RecipeIngredient recipeIngredient) {recipeIngredientRepository.delete(recipeIngredient);}
}
