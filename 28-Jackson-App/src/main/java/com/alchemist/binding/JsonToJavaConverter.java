package com.alchemist.binding;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToJavaConverter {
    public static void main(String[] args) {
        try {
            File f = new File("Customer.json");
            ObjectMapper mapper = new ObjectMapper();
            Customer c = mapper.readValue(f, Customer.class);
            
            // Print the converted Java object
            System.out.println("Customer Details:");
            System.out.println(c);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}