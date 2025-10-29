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

		/*
		 * // Here you can later add logic to fetch user from DB if
		 * (username.equals("Rohit")) { return new User( "Rohit", "{noop}admin@123", //
		 * {noop} means no password encoding Collections.singletonList(() ->
		 * "ROLE_ADMIN") // Authority ); } else { throw new
		 * UsernameNotFoundException("User not found: " + username); }
		 */
    	System.out.println("......Called.........");
    	if (username.equals("Rohit")) {
    	  return new User("Rohit",
    			  "{noop}admin@123",         //{noop} means no password encoder
    			  Collections.emptyList());  //Use Collections.emptyList() when role based security not implemented
    	}else {
    	 throw new UsernameNotFoundException("User not found: " + username);
    	}
    }
}
