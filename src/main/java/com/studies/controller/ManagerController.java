
package com.studies.controller;

import com.studies.model.RegisteredUser;
import com.studies.service.RegisteredUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagerController {
    @Autowired
    RegisteredUserService service;
    
    @RequestMapping(value="/admin/userlist", method = RequestMethod.GET)
    public ModelAndView openUserList(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RegisteredUser user = service.findUserByUsername(auth.getName());
        List<RegisteredUser> users = service.getUsers();
        modelAndView.addObject("users", users);
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/userlist");
        return modelAndView;
    }

    @RequestMapping(value="/admin/remove", method = RequestMethod.POST)
    public ModelAndView deleteUser(@RequestParam("uname") String uname){
        ModelAndView userlistModel = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName().equals(uname)) {
            userlistModel = openUserList();
            userlistModel.addObject("actionMessage", "Vartotojas yra prisijungęs");
        } else {
            service.removeUser(service.findUserByUsername(uname));
            userlistModel = openUserList();
            userlistModel.addObject("actionMessage", "Vartotojas pašalintas");
        }
        return userlistModel;
    }
}
