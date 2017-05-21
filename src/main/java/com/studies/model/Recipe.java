package com.studies.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    private Double cookingTime;

    @NotNull(message = "*Nepasirinkta kategorija")
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

   // @Pattern(regexp = "([0-9]*)", message = "Laukas turi būti sudarytas tik iš skaičių")
  //  @NotEmpty(message = "*Neįvestas porcijų skaičius")
    @Column(name = "number_of_servings")
    private Integer numberOfServings;

    @Column(name = "creation_date")
    private Date creationDate;

    //Pataisyti
 //   @NotEmpty(message = "*Nepridėtas paveikslėlis")
    @Column(name = "image")
    //private SerialBlob image;
    @Lob
    private byte[] image;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "favourite_count")
    private Integer favouriteCount;

    @Column(name = "unseen_days")
    private Integer unseenDays;

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

    public Double getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Double cookingTime) {
        this.cookingTime = cookingTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(Integer favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public Integer getUnseenDays() {
        return unseenDays;
    }

    public void setUnseenDays(Integer unseenDays) {
        this.unseenDays = unseenDays;
    }
}
