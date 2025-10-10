package com.alchemist.beans;

public class DieselEngine implements IEngine {

	@Override
	public int start() {
		System.out.println("Diesel Engine Started");
		return 1;
	}

}
