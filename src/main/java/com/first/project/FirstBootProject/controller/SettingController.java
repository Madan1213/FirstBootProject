package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.repository.ContactRepository;
import com.first.project.FirstBootProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class SettingController
{

    UserRepository repository;

    ContactRepository contactRepository;

    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SettingController(UserRepository repository, ContactRepository contactRepository , BCryptPasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.contactRepository =contactRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Show Password Change Page
    @GetMapping("/setting")
    public String settingPage()
    {
        return "/user/Setting";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal)
    {
        User user = repository.findUserByEmail(principal.getName());

        return "redirect:/user/setting";
    }
}
