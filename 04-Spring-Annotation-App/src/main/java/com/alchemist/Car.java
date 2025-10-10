package com.alchemist;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Car {
	public Car() {
		System.out.println("Car Constructor");
	}
}
