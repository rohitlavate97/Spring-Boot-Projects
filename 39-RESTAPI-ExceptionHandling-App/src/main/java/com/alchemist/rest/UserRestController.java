package com.alchemist.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.exception.UserNotFoundException;

@RestController
public class UserRestController {
	@GetMapping("/user/{userId}")
	public String getUserName(@PathVariable("userId") Integer userId) throws UserNotFoundException {
		if(userId == 100) {
			return "john";
		}else if(userId == 200) {
			return "smith";
		}else {
			throw new UserNotFoundException("User not Found");
		}
		
	}

}
