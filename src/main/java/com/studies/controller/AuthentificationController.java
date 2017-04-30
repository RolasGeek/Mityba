package com.studies.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.studies.model.RegisteredUser;
import com.studies.service.RegisteredUserService;

@Controller
public class AuthentificationController {
	@Autowired
	RegisteredUserService service;
	

	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET) 
	public ModelAndView register() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("registeredUser", new RegisteredUser());
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid RegisteredUser user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		RegisteredUser userExists = service.findUserByUsername(user.getUsername());
		if (userExists != null) {
			bindingResult
					.rejectValue("username", "error.user",
							"Vartotojas tokiu prisijungimo vardu jau egzistuoja");
		}
		if (bindingResult.hasErrors()) {
			
			modelAndView.setViewName("registration");
		} else {
			service.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("registeredUser", new RegisteredUser());
			modelAndView.setViewName("registration");
			
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		RegisteredUser user = service.findUserByUsername(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}
	
	
}
