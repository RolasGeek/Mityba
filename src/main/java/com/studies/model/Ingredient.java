
package com.studies.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "([a-zA-Z0-9]*)", message = "Laukas turi būti sudarytas tik iš raidžių ir skaičių")
    @NotEmpty(message = "*Neįvestas ingrediento pavadinimas")
    @Column(name= "name")
    private String name;
    
    @NotNull(message = "*Neįvestas matavimo vienetas")
    @Column(name= "measure_unit")
    @Enumerated(EnumType.STRING)
    private Measure measureUnit;
    
    public Long getId() {
        return id;
    }
    public void setId(Long _id) {
        this.id = _id;
    }
    public String getName() {
        return name;
    }
    public void setName(String _name) {
        this.name = _name;
    }
    public Measure getMeasureUnit() {
        return measureUnit;
    }
    public void setMeasureUnit(Measure _measureUnit) {
        this.measureUnit = _measureUnit;
    }
}
