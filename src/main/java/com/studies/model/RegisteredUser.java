
package com.studies.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

@Entity
@Table(name = "registereduser")
public class RegisteredUser {
	@NotEmpty(message = "*Vardas turi būti įvestas")
	@Column(name = "first_name")
	@Pattern(regexp = "([a-zA-Z0-9]*)", message ="Laukas turi būti sudarytas tik iš skaičių ir raidžių")
	private String firstName;
	
	@Pattern(regexp = "([a-zA-Z0-9]*)", message ="Laukas turi būti sudarytas tik iš skaičių ir raidžių")
	@NotEmpty(message = "*Pavardė turi būti įvesta")
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name= "password")
	@Transient
	@Length(min = 5, message = "*Slaptažodis turi būti bent 5 simbolių")
	@NotEmpty(message = "*Slaptažodis turi būti įvestas")
	private String password;
	
	@Id
	@Pattern(regexp = "([a-zA-Z0-9]*)", message ="Laukas turi būti sudarytas tik iš skaičių ir raidžių")
	@NotEmpty(message = "*Neįvestas vartotojo vardas")
	@Length(min = 5, max=30,  message="Vartotojo vardas turi būti nuo 5 iki 30 simbolių")
	@Column(name = "username")
	private String username;
	
	@Email(message = "*Klaidingas elektroninis paštas")
	@NotEmpty(message = "*Neįvestas elektroninis paštas")
	@Column(name= "email")
	private String email;
	
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	@Column(name = "active")
	private int active;
	
	@Column(name= "user_level")
	private Integer userLevel;
	
	@Column(name = "login_date")
	private Date loginDate;
	
	@Column(name = "token")
	private String token;
	
	@Column(name= "valid_to")
	private String validTo;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}