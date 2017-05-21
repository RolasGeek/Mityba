package com.studies.controller;

import com.studies.model.RegisteredUser;
import com.studies.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class ActiveUserController {
    @Autowired
    RegisteredUserService rUserService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(value="/activeUser/userEdit", method = RequestMethod.GET)
    public ModelAndView shoUserEditPage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = rUserService.findUserByUsername(auth.getName());
        modelAndView.addObject("userLevel", user.getUserLevel());
        modelAndView.addObject("user", user);
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

    public String validatePassword(String current, String inputedPassword, String newPassword, String repeatPassword){
        if (encoder.matches(inputedPassword, current) && newPassword.equals(repeatPassword)){
            return encoder.encode(newPassword);
        }

        return null;
    }
}
