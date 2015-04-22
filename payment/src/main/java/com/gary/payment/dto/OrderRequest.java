package com.gary.payment.dto;

import java.io.Serializable;
import java.util.Map;

public class OrderRequest implements Serializable {
	private static final long serialVersionUID = -8122305109968874392L;
	
	private String payUrl;
	
	private Map<String, ?> paramMap;
	
	private String orderNo;

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public Map<String, ?> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, ?> paramMap) {
		this.paramMap = paramMap;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
