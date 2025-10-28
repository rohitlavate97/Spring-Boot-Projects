package com.alchemist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alchemist.model.InterestRate;

@RestController
@RequestMapping("/rate")
public class RateController {

    @GetMapping("/{type}")
    public ResponseEntity<InterestRate> getRateByType(@PathVariable("type") String type) {

        System.out.println("****** Rate Service Called ******");

        if(type.equalsIgnoreCase("HOME")) {
            return ResponseEntity.ok(new InterestRate(101, "HOME", 7.5));
        }
        else if(type.equalsIgnoreCase("PERSONAL")) {
            return ResponseEntity.ok(new InterestRate(102, "PERSONAL", 11.2));
        }
        else if(type.equalsIgnoreCase("CAR")) {
            return ResponseEntity.ok(new InterestRate(103, "CAR", 9.0));
        }

        // Simulate service failure on unknown types
        throw new RuntimeException("Rate not found for type: " + type);
    }
}
