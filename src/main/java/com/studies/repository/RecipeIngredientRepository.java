package com.studies.repository;

import com.studies.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("recipeIngredientRepository")
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findAllByRecipeId(Long id);
    List<RecipeIngredient> findAllByIngredientId(Long id);
}
