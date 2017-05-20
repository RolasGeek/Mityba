
package com.studies.controller;

import com.studies.model.*;
import com.studies.service.RecipeIngredientService;
import com.studies.service.RecipeService;
import com.studies.service.RegisteredUserService;
import com.studies.service.IngredientService;

import java.util.ArrayList;
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
    @Autowired
    RecipeService rService;
    @Autowired
    RecipeIngredientService riService;

    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    
    @RequestMapping(value="/admin/userlist", method = RequestMethod.GET)
    public ModelAndView openUserList(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        List<RegisteredUser> users = rUserService.getUsers();
        modelAndView.addObject("users", users);
        modelAndView.addObject("userLevel", user.getUserLevel());
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
        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/ingredientlistedit");
        return modelAndView;
    }

    // recepto kurimo lango atidarymas
    @RequestMapping(value="/admin/recipeCreate", method = RequestMethod.GET)
    public ModelAndView recipeCreate(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        List<Ingredient> ingredients = iService.getIngredients();

        modelAndView.addObject("categories", Category.values());
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("recipe", new Recipe());
        modelAndView.addObject("recipeIngredients", recipeIngredients);
        modelAndView.addObject("tempRecipeIngredient", new RecipeIngredient());
        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.setViewName("admin/recipeCreate");
        return modelAndView;
    }

    @RequestMapping(value="/admin/recipeCreate", method = RequestMethod.POST)
    public ModelAndView addRecipeIngredient(@Valid RecipeIngredient tempRecipeIngredient, Recipe recipe, BindingResult bindingResult){
        ModelAndView modelAndView = null;
        modelAndView = recipeCreate();
        if (!bindingResult.hasErrors()){
            if(tempRecipeIngredient.getAmount() != null){
                recipeIngredients.add(tempRecipeIngredient);
                modelAndView.addObject("actionMessage", "Ingredientas pridėtas");
            }
            else{
                modelAndView.addObject("actionMessage", "Ingredientas nepridėtas");
            }
        }
        else{
            modelAndView.addObject("actionMessage", "Ingredientas nepridėtas");
        }

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

    // recepto sukurimas
    @RequestMapping(value="/admin/createRecipe", method = RequestMethod.POST)
    public ModelAndView createRecipe(@Valid Recipe recipe, BindingResult bindingResult){
        ModelAndView recipeListModel = null;
        if (!bindingResult.hasErrors()){
            rService.saveRecipe(recipe);
            List<Recipe> recipes = rService.getRecipes();
            Long id = recipes.get(recipes.size() - 1).getId();

            for (RecipeIngredient ri : recipeIngredients){
                ri.setRecipeId(id);
                riService.saveRecipeIngredient(ri);
            }
            recipeIngredients = new ArrayList<>();
            recipeListModel = recipeCreate();
            recipeListModel.addObject("RecipeActionMessage", "Receptas pridėtas");
        }
        else{
            recipeListModel = recipeCreate();
            recipeListModel.addObject("RecipeActionMessage", "Receptas nesukurtas");
        }
        return recipeListModel;
    }

}
