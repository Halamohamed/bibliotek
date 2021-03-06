package com.example.bibliotek.controller;

import com.example.bibliotek.entities.Users;
import com.example.bibliotek.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/books/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Find all users response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping
    public ResponseEntity<List<Users>> findAllUsers(String name){
        return ResponseEntity.ok(userService.findAll(name));
    }

    /**
     * Find user by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Secured({"ROLE_ADMIN","ROLE_USER","ROLE_EDITOR"})
    @GetMapping("/{id}")
    public ResponseEntity<Users> findUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Save user response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @Secured({"ROLE_ADMIN","ROLE_USER","ROLE_EDITOR"})
    @PostMapping
    public ResponseEntity<Users> saveUser(@Validated @RequestBody Users user){
        return ResponseEntity.ok(userService.save(user));
    }

    /**
     * Update user.
     *
     * @param id   the id
     * @param user the user
     */
    @Secured({"ROLE_ADMIN","ROLE_USER","ROLE_EDITOR"})
    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @Validated @RequestBody Users user){
        userService.update(id,user);
    }

    /**
     * Delete user.
     *
     * @param id the id
     */
    @Secured({"ROLE_ADMIN","ROLE_EDITOR"})
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.delete(id);
    }
}
