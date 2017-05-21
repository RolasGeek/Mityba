package com.studies.controller;

import com.studies.model.RegisteredUser;
import com.studies.model.UserIngredient;
import com.studies.service.IngredientService;
import com.studies.service.RegisteredUserService;
import com.studies.service.UserIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ActiveUserController {
    @Autowired
    RegisteredUserService rUserService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    UserIngredientService uiService;
    @Autowired
    IngredientService iService;

    @RequestMapping(value="/activeUser/userEdit", method = RequestMethod.GET)
    public ModelAndView shoUserEditPage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("user", user);
        modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
        modelAndView.setViewName("activeUser/userEdit");
        return modelAndView;
    }

    @RequestMapping(value = "activeUser/editUserInfo/{action}", method = RequestMethod.POST)
    public ModelAndView editUserInfo(@ModelAttribute(name="currentPassword") String currentPassword,
                                     @ModelAttribute(name="newPassword") String newPassword,
                                     @ModelAttribute(name="repeatPassword") String repeatPassword,
                                     @PathVariable("action") Integer action,
                                     @Valid RegisteredUser user, BindingResult bindingResult){
        ModelAndView modelAndView = shoUserEditPage();
        if (!bindingResult.hasErrors()){
            if (action == 1){
                rUserService.updateUser(user);
                modelAndView.addObject("actionMessage", "Duomenys sėkmingai išsaugoti");
            }
            else if (action == 2){
                RegisteredUser u = rUserService.findUserByUsername(user.getUsername());
                if (validatePassword(u.getPassword(), currentPassword, newPassword, repeatPassword) != null){
                    user.setPassword(newPassword);
                    rUserService.saveUser(user);
                    modelAndView.addObject("actionPasswordMessage", "Duomenys sėkmingai išsaugoti");
                }
                else{
                    modelAndView.addObject("actionPasswordMessage", "Blogai įvestas slaptažodis");
                }
            }
        }
        else if (action == 1){
            modelAndView.addObject("actionMessage", "Duomenys nebuvo išsaugoti");
        }
        else if (action == 2){
            modelAndView.addObject("actionPasswordMessage", "Duomenys nebuvo išsaugoti");
        }
        return modelAndView;
    }

    @RequestMapping(value="/activeUser/ProductListEdit", method = RequestMethod.GET)
    public ModelAndView ProductListEdit(){
        ModelAndView modelAndView = new ModelAndView();
        RegisteredUser user = rUserService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("user", user);
        modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
        modelAndView.addObject("userIngredients", uiService.findUserIngredientByUsername(user.getUsername()));
        modelAndView.addObject("userIngredient", new UserIngredient());
        modelAndView.addObject("editedUserIngredient", new UserIngredient());
        modelAndView.addObject("ingredients", iService.getIngredients());
        modelAndView.setViewName("activeUser/productListEdit");
        return modelAndView;
    }

    @RequestMapping(value="/activeUser/editProductList/{action}", method = RequestMethod.POST)
    public ModelAndView editProductList(@Valid UserIngredient userIngredient,
                                        @Valid UserIngredient editedUserIngredient,
                                        @RequestParam("index") Integer index,
                                        @RequestParam("id") Long id,
                                        @RequestParam("amount") Double amount,
                                        @PathVariable("action") Integer action,
                                        BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        boolean exists = false;
        RegisteredUser user = rUserService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!bindingResult.hasErrors()){
            if(action == 1){ //addNew

            }
            else if (action == 2){ //edit

            }
            else if (action == 3){ //delete

            }
        }
        modelAndView = ProductListEdit();
        return modelAndView;
    }


    @RequestMapping(value="/activeUser/ProductListCreate", method = RequestMethod.GET)
    public ModelAndView ProductListCreate(){
        ModelAndView modelAndView = new ModelAndView();
        RegisteredUser user = rUserService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("user", user);
        modelAndView.addObject("hasList", (uiService.findUserIngredientByUsername(user.getUsername()).size() > 0));
        modelAndView.addObject("userIngredients", uiService.findUserIngredientByUsername(user.getUsername()));
        modelAndView.addObject("userIngredient", new UserIngredient());
        modelAndView.addObject("ingredients", iService.getIngredients());
        modelAndView.setViewName("activeUser/productListCreate");
        return modelAndView;
    }

    @RequestMapping(value="/activeUser/createProductList", method = RequestMethod.POST)
    public ModelAndView createProductList(@Valid UserIngredient userIngredient,
                                     BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        boolean exists = false;

        RegisteredUser user = rUserService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!bindingResult.hasErrors()  && userIngredient.getAmount() > 0 && userIngredient.getIngredientId() != null){
            userIngredient.setUsername(user.getUsername());

            List<UserIngredient> userIngredients = uiService.findUserIngredientByUsername(user.getUsername());
            for (UserIngredient u : userIngredients){
                if (u.getIngredientId() == userIngredient.getIngredientId()){
                    exists = true;
                }
            }
            if (!exists){
                uiService.saveUserIngredient(userIngredient);
                modelAndView = ProductListCreate();
                modelAndView.addObject("actionMessage", "Ingredientas pridėtas");
            }
            else{
                modelAndView = ProductListCreate();
                modelAndView.addObject("actionMessage", "Toks ingredientas jau yra pridėtas");
            }
        }
        else{
            modelAndView = ProductListCreate();
            modelAndView.addObject("actionMessage", "Ingredientas nepridėtas");
        }
        return modelAndView;
    }


    public String validatePassword(String current, String inputedPassword, String newPassword, String repeatPassword){
        if (encoder.matches(inputedPassword, current) && newPassword.equals(repeatPassword)){
            return encoder.encode(newPassword);
        }

        return null;
    }
}
