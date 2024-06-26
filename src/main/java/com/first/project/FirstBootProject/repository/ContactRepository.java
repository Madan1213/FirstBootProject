package com.first.project.FirstBootProject.repository;

import com.first.project.FirstBootProject.entities.Contact;
import com.first.project.FirstBootProject.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>
{
    @Query("from Contact c where c.user.id=:userId")
    public Page<Contact> findContactByUser(int userId, Pageable pageable);

    //For Searching
    List<Contact> findByNameContainingAndUser(String name, User user);
}
