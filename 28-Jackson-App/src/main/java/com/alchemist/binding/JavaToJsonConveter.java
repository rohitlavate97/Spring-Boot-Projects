package com.alchemist.binding;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaToJsonConveter {
	public static void main(String[] args) throws StreamWriteException, DatabindException, IOException {
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
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File("Customer.json"), c);	
		System.out.println("Json file is created");
	}

}
