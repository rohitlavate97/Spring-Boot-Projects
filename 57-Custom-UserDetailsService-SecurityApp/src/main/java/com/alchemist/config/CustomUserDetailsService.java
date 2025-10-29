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
        System.out.println("......Called......... Username: " + username);

        // ✅ Must match exactly (case sensitive by default)
        if (username.equals("Rohit")) {
            return User
                    .withUsername("Rohit")
                    .password("{noop}admin@123") // No password encoder
                    .authorities(Collections.emptyList()) // no roles yet
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
