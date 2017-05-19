package com.studies.service;

import com.studies.model.UserIngredient;

import java.util.List;

public interface UserIngredientService {
    UserIngredient findUserIngredientById(Long id);
    void saveUserIngredient(UserIngredient userIngredient);
    void removeUserIngredient(UserIngredient userIngredient);
    List<UserIngredient> getUserIngredient();
}
