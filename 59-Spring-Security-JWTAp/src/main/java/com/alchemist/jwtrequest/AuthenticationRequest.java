package com.alchemist.jwtrequest;

import lombok.Data;

@Data
public class AuthenticationRequest {
	private String username;
	private String password;
}
