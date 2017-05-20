package com.studies.repository;

import com.studies.model.UserIngredient;
import com.studies.model.UserIngredientKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userIngredientRepository")
public interface UserIngredientRepository extends JpaRepository<UserIngredient, Long> {
    List<UserIngredient> findAll();
    List<UserIngredient> findAllByUsername(String username);
    List<UserIngredient> findAllByIngredientId(Long id);
}