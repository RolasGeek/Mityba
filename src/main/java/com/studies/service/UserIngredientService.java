package com.studies.service;

import com.studies.model.UserIngredient;
import com.studies.model.UserIngredientKey;

import java.util.List;

public interface UserIngredientService {
    List<UserIngredient> findUserIngredientByUsername(String username);
    List<UserIngredient> findUserIngredientByIngredientId(Long id);
    void saveUserIngredient(UserIngredient userIngredient);
    void removeUserIngredient(UserIngredient userIngredient);
    List<UserIngredient> getUserIngredient();
}
