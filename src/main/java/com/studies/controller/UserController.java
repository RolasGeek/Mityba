package com.studies.controller;
import com.studies.model.*;
import com.studies.service.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 Neprisijungusio vartotojo
 */
@Controller
public class UserController {
    @Autowired
    RecipeService rService;
    @Autowired
    IngredientService iService;
    @Autowired
    RecipeIngredientService riService;
    @Autowired
    RegisteredUserService rUserService;
    @Autowired
    FavouriteRecipeService frService;
    @Autowired
    UserIngredientService uiService;

    private List<UserIngredient> userIngredients = new ArrayList<>();
    private List<Ingredient> ingredientslist = new ArrayList<>();

    @RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") Long imageId) throws IOException {

        Recipe r = rService.findRecipeById(imageId);
        byte[] imageContent = r.getImage();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value={"/user/productInput"}, method = RequestMethod.GET)
    public ModelAndView openProductInput() {
        ModelAndView modelAndView = new ModelAndView();
        //-------------------------------------------------
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());

        modelAndView = fillModel(modelAndView, user);
        //------------------------
        List<Ingredient> ingredients = iService.getIngredients();
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("measure", Measure.values());
        modelAndView.addObject("ingredient", new Ingredient());
        modelAndView.addObject("uingredient", new UserIngredient());
        modelAndView.addObject("ilist", ingredientslist);
        modelAndView.addObject("uilist", userIngredients);
        modelAndView.setViewName("user/productInput");
        return modelAndView;
    }

    @RequestMapping(value="/user/productInput", method = RequestMethod.POST)
    public ModelAndView addUserIngredient(@Valid UserIngredient uingredient, Ingredient ingredient,BindingResult bindingResult) {
        ModelAndView userIngredientListModel = null;
        List<Ingredient> il = iService.getIngredients();
        for(Ingredient i: il){
            if (i.getName().equals(ingredient.getName())) {
                ingredient.setId(i.getId());
                uingredient.setIngredientId(ingredient.getId());
            }
        }
        if (!bindingResult.hasErrors()) {
            if (uingredient.getAmount() != null) {
                if (!ingredientExistsInUserIngredientList(uingredient)) {
                    userIngredients.add(uingredient);
                    ingredientslist.add(ingredient);
                    userIngredientListModel = openProductInput();
                    userIngredientListModel.addObject("actionMessage", "Ingredientas " + ingredient.getName() + " įtrauktas ");
                } else {
                    userIngredientListModel = openProductInput();
                    userIngredientListModel.addObject("actionMessage", "Ingredientas yra sąraše");
                }
            }
        } else {
            userIngredientListModel = openProductInput();
            userIngredientListModel.addObject("actionMessage", "Ingredientas nepridėtas");
        }
        return userIngredientListModel;
    }

    @RequestMapping(value = "/user/recipeInfo/{recipe_id}", method = RequestMethod.GET)
    public ModelAndView getRecipeInfo(@PathVariable("recipe_id") Long recipeId) {
        ModelAndView modelAndView = new ModelAndView();
        Recipe r = rService.findRecipeById(recipeId);
        List<RecipeIngredient> rIngredients = riService.findRecipeIngredientsByRecipeId(recipeId);
        List<Ingredient> list = new ArrayList<>();
        List<Ingredient> listOfIngredients = iService.getIngredients();
        for (RecipeIngredient recipeI : rIngredients){
            for(Ingredient ing : listOfIngredients) {
                if (recipeI.getIngredientId().equals(ing.getId())) {
                    list.add(ing);
                }
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        modelAndView = fillModel(modelAndView, user);
        modelAndView.addObject("recipe", r);
        modelAndView.addObject("rIngredients", rIngredients);
        modelAndView.addObject("ingredients", list);
        modelAndView.addObject("frecipe", new FavouriteRecipe());
        modelAndView.setViewName("user/recipeInfo");
        return modelAndView;
    }

    @RequestMapping(value="/user/recipeInfo/like/{rec_id}", method = RequestMethod.GET)
    public ModelAndView addFavouriteRecipe(@Valid FavouriteRecipe fr, BindingResult bindingResult, @PathVariable("rec_id") Long recId) {
        ModelAndView modelAndView = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        List<FavouriteRecipe> frlist = frService.findFavouriteRecipeByRecipeId(recId);
        boolean flag = false;
        if (!frlist.isEmpty()) {
            for (FavouriteRecipe f : frlist) {
                if (f.getUsername().equals(user.getUsername())) {
                    flag = true;
                }
            }
        }
        if (!bindingResult.hasErrors() && !flag) {
            fr.setUsername(user.getUsername());
            fr.setRecipeId(recId);
            frService.saveFavouriteRecipe(fr);
            Recipe receptukas = rService.findRecipeById(recId);
            int sk = receptukas.getFavouriteCount();
            receptukas.setFavouriteCount(sk + 1);
            rService.updateRecipe(receptukas);
            modelAndView = getRecipeInfo(recId);
            modelAndView.addObject("actionMessage", "Pamėgtas");
        } else {
            modelAndView = getRecipeInfo(recId);
            modelAndView.addObject("actionMessage", "Jau yra mėgstamas");
        }
        return modelAndView;
    }

    @RequestMapping(value={"/user/recipesByProducts"}, method = RequestMethod.GET)
    public ModelAndView showRecipesByProducts() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        modelAndView = fillModel(modelAndView, user);
            //-------------------------------------------------
        List<Recipe> recipesLIST = new ArrayList<>();
        List<Recipe> recipes = rService.getRecipes();
        int sk = 0;
        for (Recipe r : recipes) {
            List<RecipeIngredient> ril = riService.findRecipeIngredientsByRecipeId(r.getId());
            if (ril.size() <= ingredientslist.size()) {
                for (RecipeIngredient ri : ril) {
                    for(int j = 0; j < ingredientslist.size(); j++) {
                        Ingredient i = ingredientslist.get(j);
                        UserIngredient ui = userIngredients.get(j);
                        if (ri.getIngredientId() == i.getId() && ri.getAmount() <= ui.getAmount()) {
                            sk++;
                        }
                    }
                }
                if (sk == ril.size()){
                    recipesLIST.add(r);
                }
                sk = 0;
            }
        }
        //------------------------
        modelAndView.addObject("recipes", recipesLIST);
        modelAndView.setViewName("user/recipesByProducts");
        ingredientslist.removeAll(ingredientslist);
        userIngredients.removeAll(userIngredients);
        return modelAndView;
    }

    @RequestMapping(value={"/user/recipesByPopularity"}, method = RequestMethod.GET)
    public ModelAndView showRecipesByPopularity() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        modelAndView = fillModel(modelAndView, user);
        //-------------------------------------------------
        List<Recipe> allRecipes = rService.getRecipes();
        List<Recipe> recipes = new ArrayList<>();
        for (Recipe r : allRecipes) {
            if (r.getFavouriteCount() >= 10 && r.getViewCount() >= 10) {
                recipes.add(r);
            }
        }
        //------------------------
        modelAndView.addObject("recipes", recipes);
        modelAndView.setViewName("user/recipesByPopularity");
        return modelAndView;
    }

    @RequestMapping(value={"/user/recipesByNewness"}, method = RequestMethod.GET)
    public ModelAndView showRecipesByNewness() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        modelAndView = fillModel(modelAndView, user);
        List<Recipe> allRecipes = rService.getRecipes();
        List<Recipe> recipes = new ArrayList<>();
        Calendar end = Calendar.getInstance();
        Date endDate = end.getTime();
        long endTime = endDate.getTime();
        for (Recipe r : allRecipes) {
            Date start = r.getCreationDate();
            long startTime = start.getTime();
            long diffTime = endTime - startTime;
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            if (diffDays < 30) {
                recipes.add(r);
            }
        }
        modelAndView.addObject("recipes", recipes);
        modelAndView.setViewName("user/recipesByNewness");
        return modelAndView;
    }

    private boolean ingredientExistsInUserIngredientList(UserIngredient r){
        for (UserIngredient ri : userIngredients) {
            if (ri.getIngredientId() == r.getIngredientId()) {
                return true;
            }
        }
        return false;
    }

    private ModelAndView fillModel(ModelAndView modelAndView, RegisteredUser user){
        if (user != null){
            modelAndView.addObject("userLevel", user.getUserLevel());
            modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
        }
        else {
            modelAndView.addObject("userLevel", -1);
            modelAndView.addObject("hasList", false);
        }
        return modelAndView;
    }
}
