package com.alchemist.beans;

public class DebitCardPayment implements IPayment{
	
	public DebitCardPayment() {
		System.out.println("This is the Debit Card Payment Constructor");
	}

	@Override
	public boolean processPayment(double billAmt) {
		// TODO Auto-generated method stub
		System.out.println("Debit Card");
		return true;
	}

}
