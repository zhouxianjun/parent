package com.gary.payment.account.payeco;

public class PayEcoVoiceThirdAccount {
	/**商户号*/
	private String merchantNo;
	/**商户密码*/
	private String merchantPasswd;
	/**终端号*/
	private String termNo;
	/**SSL易联地址*/
	private String sslUrl;
	/**回调地址*/
	private String returnUrl;
	/**商户名*/
	private String merchantName;
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getMerchantPasswd() {
		return merchantPasswd;
	}
	public void setMerchantPasswd(String merchantPasswd) {
		this.merchantPasswd = merchantPasswd;
	}
	public String getTermNo() {
		return termNo;
	}
	public void setTermNo(String termNo) {
		this.termNo = termNo;
	}
	public String getSslUrl() {
		return sslUrl;
	}
	public void setSslUrl(String sslUrl) {
		this.sslUrl = sslUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
