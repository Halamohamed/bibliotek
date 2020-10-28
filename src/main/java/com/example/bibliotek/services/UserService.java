package com.example.bibliotek.services;

import com.example.bibliotek.entities.Users;
import com.example.bibliotek.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type User service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Find all users.
     *
     * @param name the name is the begging of the first or last name
     * @return the list of users matches the name
     */
    @Cacheable(value = "userCache")
    public List<Users> findAll(String name){
        log.info("Request to find all users");
        log.warn("fresh data");
        var users = userRepository.findAll();
        if(name != null){
            users = users.stream().filter(user -> user.getFirstName().toLowerCase().startsWith(name.toLowerCase()) ||
                    user.getLastName().toLowerCase().startsWith(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return users;
    }

    /**
     * Find by id user.
     *
     * @param id the user id
     * @return the user by id
     */
    @Cacheable(value = "userCache", key = "#id")
    public Users findById(String id){
        log.warn("Fresh data...");
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find the user by id %s. ", id)));
    }

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user by username
     */
    public Users findByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, // 404 -> Not found
                String.format("Could not find the user by username %s.", username)));
    }

    /**
     * Save user.
     *
     * @param user the user
     * @return the user saved
     */
    @CachePut(value = "userCache", key = "#result.id")
    public Users save(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Update user.
     *
     * @param id   the user id
     * @param user the user to update
     */
    @CachePut(value = "userCache", key = "#id")
    public void update(String id, Users user) {
        var isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().equals("ROLE_ADMIN"));
        var isCurrentUser = SecurityContextHolder.getContext().getAuthentication()
                .getName().toLowerCase().equals(user.getUsername().toLowerCase());
        if(!isAdmin && !isCurrentUser) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can only update your own details. Admin can update all users.");
        }
        if(!userRepository.existsById(id)) {
            log.error(String.format("Could not find the user by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, // 404 -> Not found
                    String.format("Could not find the user by id %s.", id));
        }
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userRepository.save(user);
    }

    /**
     * Delete user.
     *
     * @param id the user id
     */
    @CacheEvict(value = "userCache", key = "#id")
    public void delete(String id) {
        if(!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, // 404 -> Not found
                    String.format("Could not find the user by id %s.", id));
        }
        userRepository.deleteById(id);
    }



}
