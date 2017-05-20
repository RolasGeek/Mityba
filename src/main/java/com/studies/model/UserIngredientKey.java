package com.studies.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

public class UserIngredientKey implements Serializable {
    private Long ingredientId;
    private String username;
}