package com.studies.model;

import javax.persistence.*;

@Entity
@Table(name = "favouriteRecipe")
public class FavouriteRecipe {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "recipename")
    private String recipename;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecipename() {
        return recipename;
    }

    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }
}