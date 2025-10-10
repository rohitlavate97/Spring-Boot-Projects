package com.alchemist.beans;

public class PaymentService {
	private IPayment iPayment;
	
	public PaymentService() {
        System.out.println("Payment Service Constructor");
    }
	
	/*
	 * public PaymentService(IPayment iPayment) {
	 * System.out.println("Constructor Injection"); this.iPayment=iPayment; }
	 */
	
	public void setiPayment(IPayment iPayment) {
		System.out.println("Setter Injection");
		this.iPayment = iPayment;
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
