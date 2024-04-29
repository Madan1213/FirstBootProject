package com.first.project.FirstBootProject.service;

import com.first.project.FirstBootProject.entities.User;
import com.first.project.FirstBootProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository)
    {
        this.repository=repository;
    }

    public User saveUser(User user)
    {
        return repository.save(user);
    }



}
