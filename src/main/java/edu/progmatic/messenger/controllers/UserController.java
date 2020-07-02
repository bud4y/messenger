package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.modell.RegDto;
import edu.progmatic.messenger.modell.User;
import edu.progmatic.messenger.services.MyUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {
    private MyUserDetailsManager userDetailsService;

    @Autowired
    public UserController(MyUserDetailsManager userDetailsService) {
        this.userDetailsService =  userDetailsService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String showRegister(Model model) {
        model.addAttribute("registration", new RegDto());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUsers( @ModelAttribute("registration") RegDto registration, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || registrationHasErrors(registration, bindingResult)) {
            return "register";
        }

        userDetailsService.createUser(new User(registration));

        return "redirect:/login";
    }

    private boolean registrationHasErrors(RegDto registration, BindingResult bindingResult) {
        boolean hasErrors = false;
        if(!registration.getPassword().equals(registration.getPasswordConfirm())){
            bindingResult.rejectValue("password", "registration.password", "Passwords do not match!");
            bindingResult.rejectValue("passwordConfirm", "registration.passwordConfirm", "Passwords do not match!");
            hasErrors = true;
        }
        if(userDetailsService.userExists(registration.getUsername())){
            bindingResult.rejectValue("username", "registration.username", "User already exists!");
            hasErrors = true;
        }
        return hasErrors;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout() {
        return "login";
    }
}
