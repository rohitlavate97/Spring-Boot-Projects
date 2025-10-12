package com.alchemist.service;

import org.springframework.stereotype.Service;

@Service
public class ReportService {
	
	public ReportService() {
		/*
		 * this class is added to showcase that object is created by IOC when @Service
		 * annotation is added on the top of the class
		 */
		System.out.println("Report Service :: Constructor");
	}

}
