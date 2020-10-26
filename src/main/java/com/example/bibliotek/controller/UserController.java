package com.example.bibliotek.controller;

import com.example.bibliotek.entities.Users;
import com.example.bibliotek.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Users>> findAllUsers(String name){
        return ResponseEntity.ok(userService.findAll(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> findUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Users> saveUser(@Validated @RequestBody Users user){
        return ResponseEntity.ok(userService.save(user));
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @Validated @RequestBody Users user){
        userService.update(id,user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.delete(id);
    }
}
