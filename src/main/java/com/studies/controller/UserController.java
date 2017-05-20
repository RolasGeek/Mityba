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

    List<UserIngredient> userIngredients = new ArrayList<>();
    List<Ingredient> ingredientslist = new ArrayList<>();

    //TODO: nenaudojamas
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
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value={"/user/productInput"}, method = RequestMethod.GET)
    public ModelAndView showProductsInput() {
        ModelAndView modelAndView = new ModelAndView();
        //-------------------------------------------------
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        if (user != null){
            modelAndView.addObject("userLevel", user.getUserLevel());
        }
        else
            modelAndView.addObject("userLevel", -1);


        List<Ingredient> ingredients = iService.getIngredients();
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("measure", Measure.values());
        modelAndView.addObject("ingredient", new Ingredient());
        modelAndView.addObject("uingredient", new UserIngredient());
        magic();
        //------------------------
        modelAndView.setViewName("user/productInput");
        return modelAndView;
    }
    @RequestMapping(value="/user/productInput", method = RequestMethod.POST)
    public ModelAndView addUserIngredient(@Valid UserIngredient uingredient,Ingredient ingredient,BindingResult bindingResult) {
        ModelAndView userIngredientListModel = null;
        if (!bindingResult.hasErrors()) {
         //   uingredient.setIngredientId(ingredient.getId());
            userIngredients.add(uingredient);
            ingredientslist.add(ingredient);
            userIngredientListModel = showProductsInput();
            userIngredientListModel.addObject("actionMessage", "Ingredientas pridėtas");
//            System.out.println("Ingrediento name - "+ingredient.getName());
//            System.out.println("Ingrediento kiekis - "+uingredient.getAmount());
           // System.out.println("Ingrediento id - "+ingredient.getId());
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        if (user != null){
            modelAndView.addObject("userLevel", user.getUserLevel());
        }
        else
            modelAndView.addObject("userLevel", -1);

        modelAndView.addObject("recipe", r);
        modelAndView.addObject("rIngredients", rIngredients);
        modelAndView.addObject("ingredients", list);
        modelAndView.setViewName("user/recipeInfo");
        return modelAndView;
    }

    public void magic(){
        for (Ingredient i : ingredientslist){
            System.out.println("Ingrediento name - "+i.getName());
        }
    }
    @RequestMapping(value={"/user/recipesByProducts"}, method = RequestMethod.GET)
    public ModelAndView showRecipesByProducts() {
        ModelAndView modelAndView = new ModelAndView();
        //-------------------------------------------------
        List<Ingredient> ingredients = iService.getIngredients();
        List<Recipe> recipes = rService.getRecipes();
        modelAndView.addObject("recipes", recipes);
        magic();
        //------------------------
        modelAndView.setViewName("user/recipesByProducts");
        return modelAndView;
    }
}
