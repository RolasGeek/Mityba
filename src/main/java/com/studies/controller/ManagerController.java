
package com.studies.controller;

import com.studies.model.RegisteredUser;
import com.studies.model.Ingredient;
import com.studies.model.Measure;
import com.studies.service.RegisteredUserService;
import com.studies.service.IngredientService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagerController {
    @Autowired
    RegisteredUserService rUserService;
    @Autowired
    IngredientService iService;
    
    @RequestMapping(value="/admin/userlist", method = RequestMethod.GET)
    public ModelAndView openUserList(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        List<RegisteredUser> users = rUserService.getUsers();
        modelAndView.addObject("users", users);
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/userlist");
        return modelAndView;
    }
    
    // ingrediento lango atidarymas
    @RequestMapping(value="/admin/ingredientlistedit", method = RequestMethod.GET)
    public ModelAndView editIngredientList(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        List<Ingredient> ingredients = iService.getIngredients();
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("measure", Measure.values());
        modelAndView.addObject("ingredient", new Ingredient());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/ingredientlistedit");
        return modelAndView;
    }
    
    // vartotojo pasalinimas
    @RequestMapping(value="/admin/removeUser", method = RequestMethod.POST)
    public ModelAndView deleteUser(@RequestParam("uname") String uname){
        ModelAndView userlistModel = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName().equals(uname)) {
            userlistModel = openUserList();
            userlistModel.addObject("actionMessage", "Vartotojas yra prisijungęs");
        } else {
            rUserService.removeUser(rUserService.findUserByUsername(uname));
            userlistModel = openUserList();
            userlistModel.addObject("actionMessage", "Vartotojas pašalintas");
        }
        return userlistModel;
    }
    
    // ingrediento pridejimas
    @RequestMapping(value="/admin/ingredientlistedit", method = RequestMethod.POST)
    public ModelAndView addIngredient(@Valid Ingredient ingredient, BindingResult bindingResult) {
        ModelAndView ingredientlistModel = null;
        if (!bindingResult.hasErrors()) {
            iService.saveIngredient(ingredient);
            ingredientlistModel = editIngredientList();
            ingredientlistModel.addObject("actionMessage", "Ingredientas pridėtas");
        } else {
            ingredientlistModel = editIngredientList();
            ingredientlistModel.addObject("actionMessage", "Ingredientas nesukurtas");
        }
        return ingredientlistModel;
    }
    
    // ingrediento pasalinimas
    @RequestMapping(value="/admin/removeIngredient", method = RequestMethod.POST)
    public ModelAndView removeIngredient(@RequestParam("iid") Long iid) {
        iService.removeIngredient(iService.findIngredientById(iid));
        ModelAndView ingredientlistModel = editIngredientList();
        ingredientlistModel.addObject("actionMessage", "Ingredientas pašalintas");
        return ingredientlistModel;
    }
}