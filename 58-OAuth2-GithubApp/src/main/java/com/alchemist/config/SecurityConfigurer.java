/*
 * package com.alchemist.config;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.web.SecurityFilterChain;
 * 
 * @EnableWebSecurity
 * 
 * @Configuration public class SecurityConfigurer {
 * 
 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
 * Exception { http .authorizeHttpRequests(auth -> auth
 * .anyRequest().authenticated() ) .oauth2Login(oauth -> oauth
 * .defaultSuccessUrl("/greet", true) ); return http.build(); }
 * 
 * 
 * }
 */