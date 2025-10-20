package com.alchemist.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.binding.User;

@RestController
public class UserRestController {
	
	private  Map<Integer, User> dataMap =new HashMap<>();
	
	@PostMapping(
			value="/user",
			consumes= {"application/json"}
			)
	public ResponseEntity<String> addUser(@RequestBody User user){
		dataMap.put(user.getId(), user);
		System.out.println(dataMap);
		return new ResponseEntity<String>("User saved.....",HttpStatus.CREATED);
	}
	
	@GetMapping(
			value="/user", 
			produces= {"aplication/json"}
	)
	public User getUserByQueryParam(@RequestParam("userId") Integer userId) {
		User user = dataMap.get(userId);
		return user;
	}
	
	//example for multiple query parameters
	/*
	 * @GetMapping("/userData") public User getUserData(@RequestParam("name") String
	 * name, @RequestParam("email") String email ) { return null; }
	 */
	
	@GetMapping("/user/{id}")
	public User getUserByPathParam(@PathVariable("id")Integer userId) {
		User user = dataMap.get(userId);
		return user;
	}
	
	@GetMapping("/userData/{id}/data")
	public User getUserByPathParamUrlPattern(@PathVariable("id")Integer userId) {
		User user = dataMap.get(userId);
		return user;
	}

}
