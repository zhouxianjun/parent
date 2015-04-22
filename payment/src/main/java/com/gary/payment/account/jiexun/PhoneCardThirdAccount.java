package com.gary.payment.account.jiexun;

public class PhoneCardThirdAccount {
	private String merId;
	private String md5Key;
	private String returnUrl;
	private String payUrl;
	private String version = "2.00";
	private String currency = "RMB";
	private String retMode = "1";
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getMd5Key() {
		return md5Key;
	}
	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getRetMode() {
		return retMode;
	}
	public void setRetMode(String retMode) {
		this.retMode = retMode;
	}
}
