package com.studies.controller;
import com.studies.model.*;
import com.studies.service.RecipeIngredientService;
import com.studies.service.RecipeService;
import com.studies.service.RegisteredUserService;
import com.studies.service.IngredientService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    List<UserIngredient> userIngredients = new ArrayList<>();

    @RequestMapping(value={"/user/home"}, method = RequestMethod.GET)
    public ModelAndView showMainMenu() throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        //-------------------------------------------------
        List<Recipe> recipes = rService.getRecipes();
        modelAndView.addObject("recipes", recipes);
        modelAndView.addObject("recipe", new Recipe());
        //------------------------
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") Long imageId) throws IOException {

        Recipe r = rService.findRecipeById(imageId);
        byte[] imageContent = r.getImage();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value={"/user/productInput"}, method = RequestMethod.GET)
    public ModelAndView showProductsInput() {
        ModelAndView modelAndView = new ModelAndView();
        //-------------------------------------------------
        List<Ingredient> ingredients = iService.getIngredients();
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("measure", Measure.values());
        modelAndView.addObject("ingredient", new UserIngredient());
        //------------------------
        modelAndView.setViewName("user/productInput");
        return modelAndView;
    }
    @RequestMapping(value="/user/productInput", method = RequestMethod.POST)
    public ModelAndView addUserIngredient(@Valid UserIngredient ingredient, BindingResult bindingResult) {
        ModelAndView userIngredientListModel = null;
//        List<Ingredient> list = iService.getIngredients();
//        for (Ingredient ing: list) {
//            if (ing.getName().equals(ingredient.getName())) {
//                ingredient.setMeasureUnit(ing.getMeasureUnit());
//            }
//        }
        if (!bindingResult.hasErrors()) {
            userIngredients.add(ingredient);
            userIngredientListModel = showProductsInput();
            userIngredientListModel.addObject("actionMessage", "Ingredientas pridėtas");
        } else {
            userIngredientListModel = showProductsInput();
            userIngredientListModel.addObject("actionMessage", "Ingredientas nesukurtas");
        }
        return userIngredientListModel;
    }

    @RequestMapping(value = "/user/recipeInfo/{recipe_id}", method = RequestMethod.GET)
    public ModelAndView tsss(@PathVariable("recipe_id") Long recipeId) {
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
        modelAndView.addObject("recipe", r);
        modelAndView.addObject("rIngredients", rIngredients);
        modelAndView.addObject("ingredients", list);
        modelAndView.setViewName("user/recipeInfo");
        return modelAndView;
    }
}
