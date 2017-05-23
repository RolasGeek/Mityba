
package com.studies.controller;

import com.studies.model.*;
import com.studies.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
    @Autowired
    UserIngredientService uiService;

    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    private List<RecipeIngredient> recipeIngredients2 = new ArrayList<>();
    
    @RequestMapping(value="/admin/userlist", method = RequestMethod.GET)
    public ModelAndView openUserList(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        List<RegisteredUser> users = rUserService.getUsers();
        modelAndView.addObject("users", users);
        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
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
        modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
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
        RecipeIngredient r = new RecipeIngredient();
        modelAndView.addObject("categories", Category.values());
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("recipe", new Recipe());
        modelAndView.addObject("recipeIngredients", recipeIngredients);
        modelAndView.addObject("tempRecipeIngredient", new RecipeIngredient());
        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.setViewName("admin/recipeCreate");
        return modelAndView;
    }

    @RequestMapping(value="/admin/recipeCreate", method = RequestMethod.POST)
    public ModelAndView addRecipeIngredient(@Valid RecipeIngredient tempRecipeIngredient, Recipe recipe, BindingResult bindingResult) {
        ModelAndView modelAndView = recipeCreate();
        if (!bindingResult.hasErrors()) {
            if (tempRecipeIngredient.getAmount() != null) {
                if (!ingredientExistsInRecipeIngredientList(recipeIngredients, tempRecipeIngredient)) {
                    recipeIngredients.add(tempRecipeIngredient);
                    modelAndView.addObject("actionMessage", "Ingredientas pridėtas");
                    return modelAndView;
                } else {
                    modelAndView.addObject("actionMessage", "Toks ingredientas jau yra sąraše!");
                    return modelAndView;
                }
            }
        }
        modelAndView.addObject("actionMessage", "Ingredientas nepridėtas");
        return modelAndView;
    }
    
    // recepto redagavimo lango atidarymas
    @RequestMapping(value="/admin/recipeInfoEdit/{recipe_id}", method = RequestMethod.GET)
    public ModelAndView recipeInfoEdit(@PathVariable("recipe_id") Long recipeId){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        Recipe _recipe = rService.findRecipeById(recipeId);
        List<Ingredient> ingredients = iService.getIngredients();
        recipeIngredients2 = riService.findRecipeIngredientsByRecipeId(recipeId);
        modelAndView.addObject("categories", Category.values());
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("recipe", _recipe);
        modelAndView.addObject("recipeIngredients", recipeIngredients2);
        modelAndView.addObject("tempRecipeIngredient", new RecipeIngredient());
        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.setViewName("admin/recipeInfoEdit");
        return modelAndView;
    }
    
    @RequestMapping(value="/admin/recipeInfoEdit", method = RequestMethod.POST)
    public ModelAndView addRecipeIngredient2(@Valid RecipeIngredient tempRecipeIngredient, Recipe recipe, BindingResult bindingResult) {
        ModelAndView modelAndView = recipeInfoEdit(recipe.getId());
        if (!bindingResult.hasErrors()) {
            if (tempRecipeIngredient.getAmount() != null) {
                if (!ingredientExistsInRecipeIngredientList(recipeIngredients2, tempRecipeIngredient)) {
                    recipeIngredients2.add(tempRecipeIngredient);
                    modelAndView.addObject("actionMessage", "Ingredientas pridėtas");
                    return modelAndView;
                } else {
                    modelAndView.addObject("actionMessage", "Toks ingredientas jau yra sąraše!");
                    return modelAndView;
                }
            }
        }
        modelAndView.addObject("actionMessage", "Ingredientas nepridėtas");
        return modelAndView;
    }
    
    @RequestMapping(value="/admin/removeRecipeIngredient", method = RequestMethod.POST)
    public ModelAndView removeRecipeIngredient(@RequestParam("iid") Long iid, Recipe recipe) {
        System.out.println("recipe id:"+recipe.getId());
        System.out.println("id:"+iid);
        ModelAndView modelAndView = recipeInfoEdit(recipe.getId());
        for(RecipeIngredient r : recipeIngredients2) {
            if(r.getIngredientId() == iid) {
                System.out.println("rid"+r.getIngredientId());
                System.out.println("iid"+iid);
                recipeIngredients2.remove(r);
                modelAndView.addObject("actionMessage", "Ingredientas pašalintas");
                return modelAndView;
            }
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
    public ModelAndView createRecipe(@Valid Recipe recipe,
                                     BindingResult bindingResult,
                                     @RequestParam("randomMagic")MultipartFile multipartFile) throws IOException {
        ModelAndView recipeListModel = null;
        if (!bindingResult.hasErrors() && recipeIngredients.size() > 0){
            recipe.setImage(multipartFile.getBytes());

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

    // recepto sukurimas
    @RequestMapping(value="/admin/editRecipeInfo", method = RequestMethod.POST)
    public ModelAndView editRecipeInfo(@Valid Recipe recipe,
                                     BindingResult bindingResult,
                                     @RequestParam("randomMagic")MultipartFile multipartFile) throws IOException {
        ModelAndView recipeListModel = null;
        System.out.println(recipe.getId());
        if (!bindingResult.hasErrors() && recipeIngredients2.size() > 0){
            System.out.println("good");
            Recipe oldRecipe = rService.findRecipeById(recipe.getId());
            byte[] oldImg = oldRecipe.getImage();
            byte[] newImg = multipartFile.getBytes();
            if(newImg != null && newImg.length>0) recipe.setImage(newImg);
            else if(oldImg != null) recipe.setImage(oldImg);
            
            recipe.setCreationDate(oldRecipe.getCreationDate());
            recipe.setFavouriteCount(oldRecipe.getFavouriteCount());
            recipe.setUnseenDays(oldRecipe.getUnseenDays());
            recipe.setViewCount(oldRecipe.getViewCount());
            rService.updateRecipe(recipe);
            List<RecipeIngredient> dbris = riService.findRecipeIngredientsByRecipeId(recipe.getId());
            for(RecipeIngredient ri : dbris) {
                riService.removeRecipeIngredient(ri);
            }
            for(RecipeIngredient ri : recipeIngredients2) {
                ri.setRecipeId(recipe.getId());
                riService.saveRecipeIngredient(ri);
            }
            recipeIngredients2 = new ArrayList<>();
            recipeListModel = recipeInfoEdit(recipe.getId());
            recipeListModel.addObject("RecipeActionMessage", "Receptas pakeistas");
        } else {
            System.out.println("bad");
            recipeListModel = recipeInfoEdit(recipe.getId());
            recipeListModel.addObject("RecipeActionMessage", "Receptas nepakeistas");
        }
        return recipeListModel;
    }
    
    private boolean ingredientExistsInRecipeIngredientList(List<RecipeIngredient> ril, RecipeIngredient r){
        for (RecipeIngredient ri : ril) {
            if (ri.getIngredientId() == r.getIngredientId()) {
                return true;
            }
        }
        return false;
    }

}
