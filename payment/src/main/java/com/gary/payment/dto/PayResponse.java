package com.gary.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PayResponse implements Serializable {
	private static final long serialVersionUID = -291060385399735718L;
	
	private String orderNo;
	
	private String thirdOrderNo;
	
	private boolean success;
	
	private String result;
	
	private BigDecimal amount;
	
	private BigDecimal amountArrive;
	
	private BigDecimal fee = new BigDecimal(1);
	
	private String errorMsg;
	
	private Date responseDate = new Date();

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getThirdOrderNo() {
		return thirdOrderNo;
	}

	public void setThirdOrderNo(String thirdOrderNo) {
		this.thirdOrderNo = thirdOrderNo;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountArrive() {
		if(amountArrive == null && amount != null)
			this.amountArrive = amount.multiply(getFee());
		return amountArrive;
	}

	public void setAmountArrive(BigDecimal amountArrive) {
		this.amountArrive = amountArrive;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}
