package com.first.project.FirstBootProject.repository;

import com.first.project.FirstBootProject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    @Query("select u from User u where u.email= :email")
    User findUserByEmail(@Param("email") String email);

}
