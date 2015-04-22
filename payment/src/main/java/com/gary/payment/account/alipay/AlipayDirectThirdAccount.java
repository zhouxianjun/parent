package com.gary.payment.account.alipay;

import org.springframework.beans.factory.InitializingBean;

import com.gary.payment.util.alipay.AlipayConfig;

public class AlipayDirectThirdAccount implements InitializingBean {
	/**合作号*/
	private String partner;
	/**合作号Key*/
	private String key;
	/**支付宝帐号*/
	private String thirdAccount;
	/**支付宝支付URL*/
	private String payUrl;
	/**异步通知URL*/
	private String returnUrl;
	/**同步页面通知URL*/
	private String pageUrl;
	/**编码*/
	private String input_charset = "UTF-8";
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getThirdAccount() {
		return thirdAccount;
	}
	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getInput_charset() {
		return input_charset;
	}
	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}
	public void afterPropertiesSet() throws Exception {
		AlipayConfig.input_charset = getInput_charset();
		AlipayConfig.key = getKey();
		AlipayConfig.partner = getPartner();
	}
}
