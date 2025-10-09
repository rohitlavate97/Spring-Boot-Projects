package com.alchemist.beans;

public class CreditCardPayment implements IPayment{
	
	public CreditCardPayment() {
		System.out.println("This is the Credit Card Payment Constructor");
	}

	@Override
	public boolean processPayment(double billAmt) {
		// TODO Auto-generated method stub
		System.out.println("Debit Card");
		return true;
	}

}
