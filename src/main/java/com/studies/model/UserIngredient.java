package com.studies.model;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 Vartotojo turimi produktai
 */
@Entity
@IdClass(value = UserIngredientKey.class)
@Table(name = "userIngredient")

public class UserIngredient {

    @Id
    @Column(name = "ingredientId")
    private Long ingredientId;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "amount")
    private Double amount;

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}