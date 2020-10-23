package com.example.bibliotek.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
@Data
@Builder
public class Users {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String mail;
    private String phone;
    @Indexed(unique = true)
    private String username;
    private String password;
}
