package com.alchemist._GsonApi_App;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;

public class App {
	public static void main(String[] args) {
    	Address addr = new Address();
		addr.setCity("Ichalkaranji");
		addr.setState("Maharashtra");
		addr.setCountry("India");
		
		Customer c = new Customer();
		c.setId(1);
		c.setName("Rohit");
		c.setEmail("lavatero92@gmail.com");
		c.setPhno((long)80872179);
		c.setAddr(addr);
		
		/*
		 * Gson gson = new Gson(); String json = gson.toJson(c); System.out.println(c);
		 */
		
        File file = new File("customer.json");
        Gson gson = new Gson();
        try {
            // Write JSON to File
            FileWriter writer = new FileWriter(file);
            gson.toJson(c, writer);
            writer.close();
		
		/*Customer fromJson = gson.fromJson(json, Customer.class);
		System.out.println(fromJson);
        */
            FileReader reader = new FileReader(file);
            Customer fromJson = gson.fromJson(reader, Customer.class);
            reader.close();
            
            System.out.println("\nObject from JSON file:");
            System.out.println(fromJson);
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
}
