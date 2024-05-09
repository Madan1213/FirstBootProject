package com.first.project.FirstBootProject.controller;

import com.first.project.FirstBootProject.entities.Contact;
import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.repository.ContactRepository;
import com.first.project.FirstBootProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController
{

    private UserRepository repository;

    private ContactRepository contactRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SearchController(UserRepository repository, ContactRepository contactRepository , BCryptPasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.contactRepository =contactRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal)
    {
        User user = repository.findUserByEmail(principal.getName());
        List<Contact> contactList = contactRepository.findByNameContainingAndUser(query, user);
        return  ResponseEntity.ok(contactList);
    }
}
