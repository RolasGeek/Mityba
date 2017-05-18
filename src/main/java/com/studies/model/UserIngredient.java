package com.studies.model;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 Vartotojo turimi produktai
 */
@Entity
@Table(name = "product")
public class UserIngredient {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "([a-zA-Z0-9]*)", message = "Laukas turi būti sudarytas tik iš raidžių ir skaičių")
    @NotEmpty(message = "*Neįvestas produkto pavadinimas")
    @Column(name = "name")
    private String name;

    @Pattern(regexp = "([0-9]*)", message = "Laukas turi būti sudarytas tik iš skaičių")
    @NotEmpty(message = "*Neįvestas produkto kiekis")
    @Column(name = "amount")
    private Double amount;

    @NotNull(message = "*Neįvestas matavimo vienetas")
    @Column(name = "measure_unit")
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double _amount) {
        this.amount = _amount;
    }

    public Measure getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(Measure _measureUnit) {
        this.measureUnit = _measureUnit;
    }
}