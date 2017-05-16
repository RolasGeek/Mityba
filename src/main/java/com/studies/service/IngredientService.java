package com.studies.service;

import com.studies.model.Ingredient;
import java.util.List;

public interface IngredientService {
    public Ingredient findIngredientById(Long id);
    public void saveIngredient(Ingredient ingredient);
    public void removeIngredient(Ingredient ingredient);
    public List<Ingredient> getIngredients();
}
