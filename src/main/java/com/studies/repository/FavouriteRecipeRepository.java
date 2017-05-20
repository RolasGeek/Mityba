package com.studies.repository;

import com.studies.model.FavouriteRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("favouriteRecipeRepository")
public interface FavouriteRecipeRepository extends JpaRepository<FavouriteRecipe, Long> {
    List<FavouriteRecipe> findAll();
    List<FavouriteRecipe> findAllByUsername(String username);
    List<FavouriteRecipe> findAllByRecipeId(Long id);
}