package com.alchemist.config;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("......Called.........");
        
        // Encode password with BCrypt
        String encodedPassword = "$2a$12$.TWaQyWEMQ9y3CK7r.alauD97fF2d/rsgNhrhd2H8m5YgnPH4SYYO"; // encoded "admin@123"
        
        return new User("Rohit", encodedPassword, Collections.emptyList());
    }
}
