package com.alchemist.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Robot {
	@Autowired
	private Chip chip;
	
	public Robot() {
		System.out.println("Robot Constructor");
	}
	
	public void doWork() {
		String chipType = chip.chipType();
		if(chipType != null) {
			System.out.println("Robot is working with chip: " + chipType);
		} else {
			System.out.println("Robot is working without a chip");
		}
	}

}
