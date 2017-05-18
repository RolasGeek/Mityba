package com.studies.controller;
import com.studies.model.*;
import com.studies.service.UserIngredientService;
import com.studies.service.RecipeService;
import com.studies.service.RegisteredUserService;
import com.studies.service.IngredientService;

import java.io.*;
import java.util.List;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
/**
 Neprisijungusio vartotojo
 */
@Controller
public class UserController {
    @Autowired
    RegisteredUserService rUserService;
    @Autowired
    RecipeService rService;
    @Autowired
    UserIngredientService pService;
    @Autowired
    IngredientService iService;



    @RequestMapping(value={"/user/home"}, method = RequestMethod.GET)
    public ModelAndView showMainMenu() throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        //-------------------------------------------------
        List<Recipe> recipes = rService.getRecipes();
        modelAndView.addObject("recipes", recipes);
        byte[] bytes = recipes.get(0).getImage();
        OutputStream outputStream = null;
        outputStream =
                new FileOutputStream(new File("F:/img.jpg"));

        outputStream.write(bytes);
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
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }
//    @RequestMapping(value={"/user/recipesByProducts"}, method = RequestMethod.GET)
//    public ModelAndView showRecipesByProducts(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("user/recipesByProducts");
//        return modelAndView;
//    }
    @RequestMapping(value={"/user/productInput"}, method = RequestMethod.GET)
    public ModelAndView showProductsInput() {
        ModelAndView modelAndView = new ModelAndView();
        //-------------------------------------------------
        List<Ingredient> ingredients = iService.getIngredients();
        modelAndView.addObject("ingredients", ingredients);
        modelAndView.addObject("measure", Measure.values());
        //------------------------
        modelAndView.setViewName("user/productInput");
        return modelAndView;
    }

}
