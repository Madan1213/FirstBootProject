package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.helper.Messages;
import com.first.project.FirstBootProject.repository.UserRepository;
import com.first.project.FirstBootProject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

@Controller
public class HomeController
{
    private UserService service;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(UserService service,BCryptPasswordEncoder passwordEncoder)
    {
        this.service=service;
        this.passwordEncoder = passwordEncoder;
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
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session,
                               @RequestParam(value="agreement", defaultValue = "false") boolean agreement)
    {
        System.out.println("Main methoid");
        try {
            if(!agreement)
            {
                System.out.println("Aggreement");
                throw new Exception("You have not agreed terms & conditions");
            }
            if(result.hasErrors())
            {
                System.out.println("VALIDA"+result);
                model.addAttribute("user",user);
                return "signup";
            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User user1 = service.saveUser(user);
            System.out.println(user1);
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
