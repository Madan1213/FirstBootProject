package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserRepository repository;

    @Autowired
    public UserController(UserRepository repository)
    {
        this.repository=repository;
    }

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal)
    {
        String name = principal.getName();
        User user = repository.findUserByEmail(name);
        model.addAttribute("user",user);
        return "user/user_dashboard";
    }
}
