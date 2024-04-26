package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.helper.Messages;
import com.first.project.FirstBootProject.repository.UserRepository;
import com.first.project.FirstBootProject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

@Controller
public class HomeController
{
    private UserService service;

    @Autowired
    public HomeController(UserService service)
    {
        this.service=service;
    }

    @GetMapping("/")
    public String home(Model model)
    {
        model.addAttribute("title","Home");
        return "home";
    }

    @GetMapping("/signup")
    public String signUp(Model model)
    {
        model.addAttribute("title","Register");
        model.addAttribute("user",new User());
        return "signup";
    }

    @GetMapping("/about")
    public String about(Model model)
    {
        model.addAttribute("title","About");
        return "about";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model, HttpSession session,
                               @RequestParam(value="agreement", defaultValue = "false") boolean agreement)
    {
        try {
            if(!agreement)
            {
                throw new Exception("You have not agreed terms & conditions");
            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            User user1 = service.saveUser(user);
            model.addAttribute("user",new User());
            session.setAttribute("message", new Messages("Successfully Rsgiseter!!","alert-success"));
            return "signup";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            model.addAttribute("user",user);
            session.setAttribute("message", new Messages("Something went wrong"+e.getMessage(),"alert-danger"));
            return "signup";
        }
    }

}
