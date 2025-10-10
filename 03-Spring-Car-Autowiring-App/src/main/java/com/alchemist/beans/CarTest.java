package com.alchemist.beans;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CarTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("Beans.xml");
		CarService car=context.getBean(CarService.class);
		car.startCar();
	}

}
 