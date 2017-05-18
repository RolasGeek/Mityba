package com.studies.repository;

import com.studies.model.UserIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productRepository")
public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {
    List<UserIngredient> findAll();
    UserIngredient findById(Long id);
}