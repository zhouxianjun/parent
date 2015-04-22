package com.gary.payment;

import com.gary.payment.service.AbstractRechargeService;

public class TestRun implements Runnable{

	private AbstractRechargeService service;
	
	public TestRun(AbstractRechargeService service) {
		this.service = service;
	}
	
	public void run() {
		System.out.println(service.createOrderNo());
	}
	
}