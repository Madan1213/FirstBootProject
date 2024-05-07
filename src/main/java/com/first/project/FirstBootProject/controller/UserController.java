package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.Contact;
import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.helper.Messages;
import com.first.project.FirstBootProject.repository.ContactRepository;
import com.first.project.FirstBootProject.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserRepository repository;

    private ContactRepository contactRepository;

    @Autowired
    public UserController(UserRepository repository, ContactRepository contactRepository)
    {
        this.repository=repository;
        this.contactRepository=contactRepository;
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
                              @RequestParam("imageFile")MultipartFile multipartFile,
                              Principal principal, HttpSession session,Model model)
    {
        try{

            //Upload and Save File in contact table
            if(multipartFile.isEmpty()) {
                //If the image is empty show some message
                contact.setImage("contact.png");
            }
            else{
                contact.setImage(multipartFile.getOriginalFilename());
                File file = new ClassPathResource("/static/img/").getFile();

                Path path = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());

                Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }

            String name = principal.getName();
            User user = repository.findUserByEmail(name);

            user.getContact().add(contact);

            contact.setUser(user);
            this.repository.save(user);
            session.setAttribute("message",new Messages("SuccessFully Registered!!","success"));
            model.addAttribute("contact", new Contact());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            session.setAttribute("message",new Messages("Failed!!","danger"));
        }
        return "redirect:/user/addContact";
    }

    @GetMapping("/showContacts/{page}")
    public String showContactsPage(@PathVariable("page")Integer page, Model model,Principal principal)
    {
        String name = principal.getName();
        User user = repository.findUserByEmail(name);
        //Current Page and Page Size required
        Pageable pageable = PageRequest.of(page,5);

        Page<Contact> contacts = contactRepository.findContactByUser(user.getId(),pageable);
        model.addAttribute("contacts",contacts);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",contacts.getTotalPages());
        return "user/contact_list";
    }

    @GetMapping("/contactDetails/{cid}")
    public String contactDetails(@PathVariable("cid") Integer cid, Model model,Principal principal)
    {
        String name = principal.getName();
        User user = repository.findUserByEmail(name);
        Contact contact = contactRepository.findById(cid).get();

        if(user.getId()==contact.getUser().getId())
            model.addAttribute("contact",contact);
        return "user/contact_details";
    }

    @GetMapping("/deleteContact/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid,Principal principal)
    {
        Contact contact = contactRepository.findById(cid).get();
        String name = principal.getName();
        User user = repository.findUserByEmail(name);

        if(user.getId()==contact.getUser().getId()) {
            contact.setUser(null);
            contactRepository.delete(contact);
        }
        return "redirect:/user/showContacts/0";
    }

    @GetMapping("/editContact/{cid}")
    public String editContact(@PathVariable("cid") Integer cid, Model model , Principal principal)
    {
        Contact contact = contactRepository.findById(cid).get();
        String name = principal.getName();
        User user = repository.findUserByEmail(name);
        if(user.getId()==contact.getUser().getId())
        {
            model.addAttribute("contact",contact);
        }

        return "/user/edit_contact";
    }

    @PostMapping("/updateContact/{cid}")
    public String updateContact(@PathVariable("cid") Integer cid,
                                @ModelAttribute("contact") Contact contact,Model model , 
                                HttpSession session, Principal principal)
    {
        try{
            String name = principal.getName();
            User user = repository.findUserByEmail(name);
            contact.setUser(user);
            //contact.setCid(cid);
            contactRepository.save(contact);
            session.setAttribute("message",new Messages("Contact Updated Successfully!!","alert-success"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            session.setAttribute("message",new Messages("Failed!!","alert-danger"));
        }

        return "redirect:/user/addContact";
    }

}
