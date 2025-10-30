package com.alchemist.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.alchemist.jwtrequest.AuthenticationRequest;
import com.alchemist.jwtresponse.AuthenticationResponse;
import com.alchemist.security.CustomUserDetailsService;
import com.alchemist.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class HelloRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // ✅ Test endpoint (secured)
    @GetMapping("/hello")
    public String helloUser() {
        return "Hello, User 👋 — Your JWT is valid!";
    }

    // ✅ Authentication endpoint (public)
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
        try {
            // 1️⃣ Authenticate username & password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("❌ Invalid username or password", e);
        }

        // 2️⃣ Load user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // 3️⃣ Generate JWT token
        final String jwt = jwtUtil.generateToken(userDetails);

        // 4️⃣ Return token as JSON
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
