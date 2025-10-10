package com.alchemist.beans;

public class CarService {
	
	private IEngine engine;

	public CarService(IEngine engine) {
		this.engine = engine;
	}

	public void startCar() {
		int status=engine.start();
		if(status==1) {
			System.out.println("Car Started");
	}
		else {
			System.out.println("Car not started");
		}
	}
}
