package com.first.project.FirstBootProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController
{
    @GetMapping("/showLoginForm")
    public String showLoginForm()
    {
        System.out.println("LOGIN PAGE");
        return "login";
    }
}
