package com.first.project.FirstBootProject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "user name is required")
    @Size(min = 2,max = 20,message = "Name length should between 2 and 20")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Please enter proper mail id")
    @Column(unique = true)
    private String email;

    /*@NotBlank(message = "Password is required")
    @Size(min = 5, max = 10, message = "Password length should be between 5 and 10")*/
    private String password;
    private String role;

    private boolean enabled;
    private String imageUrl;
    @Column(length = 500)
    private String about;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
    private List<Contact> contact = new ArrayList<>();
}
