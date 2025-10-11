package com.alchemist;

public class UserDAO {
	public void init() {
		System.out.println("Getting db connection.....");
	}
	
	public void getData() {
		System.out.println("Getting the data from thd db...");
	}
	
	public void destroy() {
		System.out.println("Closing db connection.....");
	}

}
