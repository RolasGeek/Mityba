package com.studies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studies.model.Ingredient;
import java.util.List;

@Repository("ingredientRepository")
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAll();
    Ingredient findById(Long id);
}