package com.alchemist.config;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ProfileChecker implements CommandLineRunner {

    private final Environment environment;

    @Value("${app.environment:Unknown}")
    private String environmentName;

    public ProfileChecker(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        System.out.println("=====================================");
        System.out.println("🚀 Active Profiles: " + Arrays.toString(environment.getActiveProfiles()));
        System.out.println("📋 Default Profiles: " + Arrays.toString(environment.getDefaultProfiles()));
        System.out.println("🌍 Environment Name: " + environmentName);
        
        // Check specific profiles
        if (environment.acceptsProfiles("dev")) {
            System.out.println("💻 Running in DEVELOPMENT mode");
        }
        if (environment.acceptsProfiles("prod")) {
            System.out.println("🎯 Running in PRODUCTION mode");
        }
        System.out.println("=====================================");
    }
}