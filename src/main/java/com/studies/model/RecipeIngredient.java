package com.studies.model;

import com.studies.service.RecipeIngredientService;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Recepto ingredientai
 */
@Entity
@IdClass(value = RecipeIngredientKey.class)
@Table(name = "recipeIngredient")
public class RecipeIngredient {
    @Id
    //@Pattern(regexp = "([0-9]*)", message = "Laukas turi būti sudarytas tik iš skaičių")
    //@NotEmpty(message = "*Neįvestas recepto ID")
    @Column(name = "recipe_id")
    private Long recipeId;

    @Id
    //@Pattern(regexp = "([0-9]*)", message = "Laukas turi būti sudarytas tik iš skaičių")
    //@NotEmpty(message = "*Neįvestas ingrediento ID")
    @Column(name = "ingredient_id")
    private Long ingredientId;

    //@Pattern(regexp = "([0-9]*)", message = "Laukas turi būti sudarytas tik iš skaičių")
    //@NotEmpty(message = "*Neįvestas kiekis")
    @Column(name = "amount")
    private Double amount;

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
