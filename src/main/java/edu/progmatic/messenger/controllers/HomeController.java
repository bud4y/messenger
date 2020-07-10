package edu.progmatic.messenger.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String homePage(Model model) {

        return "home";
    }
    @GetMapping(value = "/rest/csref")
    public CsrfToken getCsrefToken(CsrfToken token){
        return token;
    }
}
