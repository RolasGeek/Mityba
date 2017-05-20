package com.studies.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 Receptas
 */
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    //@Pattern(regexp = "([a-zA-Z0-9]*)", message = "Laukas turi būti sudarytas tik iš raidžių ir skaičių")
    @NotEmpty(message = "*Neįvestas recepto pavadinimas")
    @Column(name = "name")
    private String name;

    //@Pattern(regexp = "([a-zA-Z0-9]*)", message = "Laukas turi būti sudarytas tik iš raidžių ir skaičių")
    @NotEmpty(message = "*Neįvestas recepto aprašymas")
    @Column(name = "description")
    private String description;

   // @Pattern(regexp = "([0-9]*)", message = "Laukas turi būti sudarytas tik iš skaičių")
   // @NotEmpty(message = "*Neįvestas gaminimo laikas")
    @Column(name = "cooking_time")
    private Double cooking_time;

    @NotNull(message = "*Nepasirinkta kategorija")
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

   // @Pattern(regexp = "([0-9]*)", message = "Laukas turi būti sudarytas tik iš skaičių")
  //  @NotEmpty(message = "*Neįvestas porcijų skaičius")
    @Column(name = "number_of_servings")
    private Integer number_of_servings;

    @Column(name = "creation_date")
    private Date creation_date;

    //Pataisyti
 //   @NotEmpty(message = "*Nepridėtas paveikslėlis")
    @Column(name = "image")
    //private SerialBlob image;
    @Lob
    private byte[] image;

    @Column(name = "view_count")
    private Integer view_count;

    @Column(name = "favourite_count")
    private Integer favourite_count;

    @Column(name = "unseen_days")
    private Integer unseen_days;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(Double cooking_time) {
        this.cooking_time = cooking_time;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getNumber_of_servings() {
        return number_of_servings;
    }

    public void setNumber_of_servings(Integer number_of_servings) {
        this.number_of_servings = number_of_servings;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getView_count() {
        return view_count;
    }

    public void setView_count(Integer view_count) {
        this.view_count = view_count;
    }

    public Integer getFavourite_count() {
        return favourite_count;
    }

    public void setFavourite_count(Integer favourite_count) {
        this.favourite_count = favourite_count;
    }

    public Integer getUnseen_days() {
        return unseen_days;
    }

    public void setUnseen_days(Integer unseen_days) {
        this.unseen_days = unseen_days;
    }
}
