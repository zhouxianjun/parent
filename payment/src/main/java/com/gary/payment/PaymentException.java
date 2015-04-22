package com.gary.payment;

public class PaymentException extends Exception {
	private static final long serialVersionUID = 2575128652435614835L;
	
	public PaymentException(String msg) {
		super(msg);
	}
}
