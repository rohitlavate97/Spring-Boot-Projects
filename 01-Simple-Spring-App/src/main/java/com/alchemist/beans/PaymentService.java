package com.alchemist.beans;

public class PaymentService {
	private IPayment iPayment;
	
	public PaymentService(IPayment iPayment) {
		this.iPayment=iPayment;
	}
	
	public void doPayment(int amt) {
		boolean status=iPayment.processPayment(amt);
		if(status) {
			System.out.println("Print Receipt");
		}else {
			System.out.println("Card declilned");
		}
	}

}
