package com.studies.service;

import com.studies.model.UserIngredient;
import com.studies.repository.UserIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class UserIngredientServiceImpl implements UserIngredientService {
    @Autowired
    private UserIngredientRepository userIngredientRep;

    @Override
    public UserIngredient findUserIngredientById(Long id) {
        return userIngredientRep.findById(id);
    }

    @Override
    public void saveUserIngredient(UserIngredient userIngredient) {
        userIngredientRep.save(userIngredient);
    }

    @Override
    public void removeUserIngredient(UserIngredient userIngredient) {
        userIngredientRep.delete(userIngredient);
    }

    @Override
    public List<UserIngredient> getUserIngredient() {
        return userIngredientRep.findAll();
    }
}
