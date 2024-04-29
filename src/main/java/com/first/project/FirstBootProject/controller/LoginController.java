package com.first.project.FirstBootProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController
{
    @GetMapping("/showLoginForm")
    public String showLoginPage()
    {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied()
    {
        return "access-denied";
    }

}
