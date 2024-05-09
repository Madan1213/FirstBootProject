package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.helper.Messages;
import com.first.project.FirstBootProject.repository.ContactRepository;
import com.first.project.FirstBootProject.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class SettingController
{

    private UserRepository repository;

    private ContactRepository contactRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SettingController(UserRepository repository, ContactRepository contactRepository , BCryptPasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.contactRepository =contactRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal)
    {
        String name = principal.getName();
        User user = repository.findUserByEmail(name);
        model.addAttribute("user",user);
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
                                 Principal principal, HttpSession session)
    {
        try{
            User user = repository.findUserByEmail(principal.getName());
            if(passwordEncoder.matches(oldPassword,user.getPassword()))
            {
                user.setPassword(passwordEncoder.encode(newPassword));
                repository.save(user);
                session.setAttribute("message", new Messages("Password Changed Successfully!!","alert-success"));
            }
            else{
                session.setAttribute("message", new Messages("Wrong old password :(","alert-danger"));
            }
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message",new Messages("Something went wrong","alert-danger"));
        }
        return "redirect:/setting";
    }
}
