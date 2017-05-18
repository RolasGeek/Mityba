package com.studies.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.studies.model.RegisteredUser;
import com.studies.service.RegisteredUserService;

@Controller
public class AuthentificationController {
    @Autowired
    RegisteredUserService service;
    
    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET) 
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("registeredUser", new RegisteredUser());
        modelAndView.setViewName("auth/registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid RegisteredUser user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        RegisteredUser userExists = service.findUserByUsername(user.getUsername());
        if (userExists != null) {
            bindingResult.rejectValue("username", "error.user",
                                                "Vartotojas tokiu prisijungimo vardu jau egzistuoja");
        }
        if (bindingResult.hasErrors()) {
                modelAndView.setViewName("registration");
        } else {
            service.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("registeredUser", new RegisteredUser());
            modelAndView.setViewName("auth/registration");
        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = service.findUserByUsername(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
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
    	modelAndView.addObject("oldDiss", false);
    	modelAndView.addObject("newDiss", true);
    	modelAndView.addObject("conDiss", true);
    	modelAndView.addObject("oldReq", true);
    	modelAndView.addObject("link", "/passreset");
    	modelAndView.setViewName("auth/passreset");
    	return modelAndView;
    }
    
    @RequestMapping(value="/passreset", method = RequestMethod.POST) 
    public ModelAndView checkData(@RequestParam(name="oldpass") String old) {
    	
    	ModelAndView modelAndView = new ModelAndView();
    	if(validateSecretData(old)) {
    			modelAndView.addObject("oldDiss", true);
            	modelAndView.addObject("newDiss", false);
            	modelAndView.addObject("conDiss", false);
            	modelAndView.addObject("oldReq", false);
            	modelAndView.addObject("oldpass", old);
            	modelAndView.addObject("link", "/passresetconfirm");
            	modelAndView.setViewName("auth/passreset");
    	} else {
    		modelAndView = loadRestoreWindow();
    		modelAndView.addObject("error", "Blogas senas slaptazodis");
    	}
    	
    	return modelAndView;
    }
    
    @RequestMapping(value="/passresetconfirm", method = RequestMethod.POST) 
    public ModelAndView checkPassword(@RequestParam(name="oldpass") String old, @RequestParam(name="newpass") String newp, @RequestParam(name="conpass") String conf) {
    	
    	ModelAndView modelAndView = new ModelAndView();
    	if(validatePassword(newp, conf) != null) {
    		System.out.println("toli");
    	} else {
    		checkData(old);
    	}
    	
    	
    	return modelAndView;
    }
    
    public boolean validateSecretData(String password) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	RegisteredUser user = service.findUserByUsername(username);
    	return encoder.matches(password, user.getPassword());
    }
    
    public String validatePassword(String pass1, String pass2) {
    	if( pass1.equals(pass2)) {
    		return encoder.encode(pass1);
    	}
    	return null;
    }
    
    
}
