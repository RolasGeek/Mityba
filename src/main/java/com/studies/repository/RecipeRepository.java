package com.studies.repository;

import com.studies.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("recipeRepository")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAll();
    Recipe findById(Long id);
}