package com.alchemist.security;

import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private Map<String, String> users = Map.of(
			"rohit", new BCryptPasswordEncoder().encode("rohit@123"),
			"admin", new BCryptPasswordEncoder().encode("admin@123")
			);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (!users.containsKey(username))
			throw new UsernameNotFoundException("User not found: " + username);

		String password = users.get(username);
		return User.withUsername(username).password(password).roles("USER").build();
	}
}