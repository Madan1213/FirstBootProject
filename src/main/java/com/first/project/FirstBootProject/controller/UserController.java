package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.Contact;
import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    //Method for adding common data to response
    @ModelAttribute
    public void addCommonData(Model model, Principal principal)
    {
        String name = principal.getName();
        User user = repository.findUserByEmail(name);
        model.addAttribute("user",user);
    }

    @GetMapping("/index")
    public String dashboard(Model model)
    {
        model.addAttribute("title","User Dashboard");
        return "user/user_dashboard";
    }

    //Open add form handler
    @GetMapping("/addContact")
    public String openContactForm(Model model)
    {
        model.addAttribute("contact", new Contact());
        return "user/add_contact";
    }

    //Save Contact Details Using Users Details
    @PostMapping("/saveContact")
    public String saveContact(@ModelAttribute("contact") Contact contact,
                              @RequestParam("image")MultipartFile multipartFile,
                              Principal principal)
    {
        try{

            //Upload and Save File in contact table
            if(multipartFile.isEmpty()) {
                //If the image is empty show some message
            }
            else{
                contact.setImage(multipartFile.getOriginalFilename());
                File file = new ClassPathResource("/static/img/contact").getFile();

                Path path = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());

                Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }

            String name = principal.getName();
            User user = repository.findUserByEmail(name);

            user.getContact().add(contact);

            contact.setUser(user);
            this.repository.save(user);

            System.out.println(contact);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "user/add_contact";
    }
}
