package com.studies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studies.model.Ingredient;
import com.studies.repository.IngredientRepository;
import java.util.List;

@Service("ingredientService")
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRep;
    
    @Override
    public Ingredient findIngredientById(Long id) {
        return ingredientRep.findById(id);
    }
    
    @Override
    public void saveIngredient(Ingredient ingredient) {
        ingredientRep.save(ingredient);
    }
    
    @Override
    public void removeIngredient(Ingredient ingredient) {
        ingredientRep.delete(ingredient);
    }
    
    @Override
    public List<Ingredient> getIngredients() {
        return ingredientRep.findAll();
    }
}
