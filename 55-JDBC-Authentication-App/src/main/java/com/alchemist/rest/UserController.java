package com.alchemist.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@GetMapping("/admin")                //method accessed by admin only
	public String admin() {
		return "<h3>Welcome Admin:)</h3>";
	}
	
	@GetMapping("/user")                //method accessed by User only
	public String user() {
		return "<h3>Hello User:)</h3>";
	}
	@GetMapping(value="/")              //Anybody can access this method
	public String welcome() {
		return "Welcome:)";
	}
}
