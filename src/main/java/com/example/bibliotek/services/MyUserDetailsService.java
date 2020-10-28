package com.example.bibliotek.services;


import com.example.bibliotek.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The type My user details service.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userService.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Users user) {
        return user.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" +authority.toString()))
                .collect(Collectors.toList());
    }


}
