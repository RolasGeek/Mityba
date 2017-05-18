package com.studies.service;

import com.studies.model.UserIngredient;
import com.studies.repository.UserIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class UserIngredientServiceImpl implements UserIngredientService {
    @Autowired
    private UserIngredientRepository productRep;

    @Override
    public UserIngredient findProductById(Long id) {
        return productRep.findById(id);
    }

    @Override
    public void saveProduct(UserIngredient product) {
        productRep.save(product);
    }

    @Override
    public void removeProduct(UserIngredient product) {
        productRep.delete(product);
    }

    @Override
    public List<UserIngredient> getProducts() {
        return productRep.findAll();
    }
}
