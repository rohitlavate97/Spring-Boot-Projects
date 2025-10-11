package com.alchemist;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

@Component
public class UserDAO{
	
	@PostConstruct
	public void init() {
		System.out.println("Getting db connection.....");
	}
	
	public void getData() {
		System.out.println("Getting the data from thd db...");
	}
	
	@PreDestroy
	public void destroy() throws Exception{
		System.out.println("Closing db connection.....");
	}

}
