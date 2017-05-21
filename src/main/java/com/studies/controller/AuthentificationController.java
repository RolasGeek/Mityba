package com.studies.controller;

import javax.validation.Valid;

import com.studies.model.Recipe;
import com.studies.service.RecipeService;
import com.studies.service.UserIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.DateUtils;

import com.studies.model.RegisteredUser;
import com.studies.service.RegisteredUserService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class AuthentificationController {
    @Autowired
    RegisteredUserService service;
    @Autowired
    RecipeService rService;
    @Autowired
    UserIngredientService uiService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/LoginView");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET) 
    public ModelAndView loadRegisterWindow() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("registeredUser", new RegisteredUser());
        modelAndView.setViewName("auth/Register");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid RegisteredUser user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        if (validateRegister(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user",
                                                "Vartotojas tokiu prisijungimo vardu jau egzistuoja");
        }
        if (bindingResult.hasErrors()) {
                modelAndView.setViewName("auth/Register");
        } else {
            service.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("registeredUser", new RegisteredUser());
            modelAndView.setViewName("auth/Register");
        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = service.findUserByUsername(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @RequestMapping(value={"/", "/mainMenu"}, method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = service.findUserByUsername(auth.getName());

        List<Recipe> recipes = rService.getRecipes();
        modelAndView.addObject("recipes", recipes);
        modelAndView.addObject("recipe", new Recipe());

        if (user != null){
            modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            modelAndView.addObject("userLevel", user.getUserLevel());
            modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));

            if (user.getUserLevel() == 1){
                modelAndView.addObject("userMessage","Content Available Only for Users with Admin Role");
            }
            else{
                modelAndView.addObject("userMessage", "Simple user");
            }
        }
        else{
            modelAndView.addObject("userLevel", -1);
            modelAndView.addObject("userMessage", "Unregistered user");
            modelAndView.addObject("hasList", false);
        }
        modelAndView.setViewName("home/mainMenu");

        return modelAndView;
    }

    @RequestMapping(value="/fakelogout", method = RequestMethod.GET )
    public ModelAndView logout(){
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().setAuthentication(null);
        modelAndView = login();
        
        return modelAndView;
    }
    
    @RequestMapping(value="/passreset", method = RequestMethod.GET) 
    public 	ModelAndView loadRestoreWindow() {
    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.addObject("userLevel", -1);
    	modelAndView.addObject("hasList", false);
		modelAndView.addObject("userDiss", false);
		modelAndView.addObject("emailDiss", false);
    	modelAndView.addObject("newDiss", true);
    	modelAndView.addObject("conDiss", true);
    	modelAndView.addObject("userReq", true);
    	modelAndView.addObject("emailReq", true);
    	modelAndView.addObject("link", "/passreset");
    	modelAndView.setViewName("auth/RestorePassword");
    	return modelAndView;
    }
    
    @RequestMapping(value="/passreset", method = RequestMethod.POST) 
    public ModelAndView checkData(@RequestParam(name="username") String username, @RequestParam(name="email") String email) {
    	
    	ModelAndView modelAndView = new ModelAndView();
    	if(validateSecretData(username, email)) {
            modelAndView.addObject("userLevel", -1);
            modelAndView.addObject("hasList", false);
            modelAndView.addObject("userDiss", true);
            modelAndView.addObject("emailDiss", true);
            modelAndView.addObject("newDiss", false);
            modelAndView.addObject("conDiss", false);
            modelAndView.addObject("userReq", false);
            modelAndView.addObject("emailReq", false);
            modelAndView.addObject("username", username);
            modelAndView.addObject("email", email);
            modelAndView.addObject("link", "/passresetconfirm");
            modelAndView.setViewName("auth/RestorePassword");
    	} else {
    		modelAndView = loadRestoreWindow();
    		modelAndView.addObject("error", "Blogi duomenys");
    	}
    	
    	return modelAndView;
    }
    
    @RequestMapping(value="/passresetconfirm", method = RequestMethod.POST) 
    public ModelAndView checkPassword(@ModelAttribute(name="username1") String username,@ModelAttribute(name="email1") String email , @RequestParam(name="newpass") String newp, @RequestParam(name="conpass") String conf) {
    	
    	ModelAndView modelAndView = new ModelAndView();
    	String encPass = validatePassword(newp, conf);
    	if(encPass!= null) {
    		RegisteredUser user = service.findUserByUsername(username);
    		user.setPassword(encPass);
    		service.updateUser(user);
    		modelAndView.setViewName("auth/EventSuccessWindow");
    		modelAndView.addObject("message", "Slaptažodis pakeistas");
    	} else {
    		checkData(username, email);
    	}
    	
    	
    	return modelAndView;
    }
    
    //Validate methods
    
    public boolean validateRegister(String username) {
    	RegisteredUser userExists = service.findUserByUsername(username);
    	return userExists != null;
    }
    
    public boolean validateSecretData(String username, String email) {
    	RegisteredUser user = service.findUserByUsername(username);
    	return email.equals(user.getEmail());
    }
    
    public String validatePassword(String pass1, String pass2) {
    	if( pass1.equals(pass2)) {
    		return encoder.encode(pass1);
    	}
    	return null;
    }
    
    
}
