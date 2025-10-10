package com.alchemist.config;
//Normally AppConfig class should be present in base package

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.alchemist")
public class AppConfig {

}
