package com.alchemist;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	public UserService() {
		System.out.println("Getting data from Redis...");
	}

}
