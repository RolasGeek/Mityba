package com.studies.service;

import com.studies.model.UserIngredient;

import java.util.List;

public interface UserIngredientService {
    UserIngredient findProductById(Long id);
    void saveProduct(UserIngredient product);
    void removeProduct(UserIngredient product);
    List<UserIngredient> getProducts();
}
