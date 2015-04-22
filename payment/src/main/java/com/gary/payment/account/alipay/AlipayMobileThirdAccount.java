package com.gary.payment.account.alipay;

import org.springframework.beans.factory.InitializingBean;

import com.gary.payment.util.alipay.AlipayConfig;

public class AlipayMobileThirdAccount implements InitializingBean {
	/**合作号*/
	private String partner;
	/**合作号Key*/
	private String key;
	/**支付宝帐号*/
	private String thirdAccount;
	/**异步通知URL*/
	private String returnUrl;
	/**支付宝公钥*/
	private String publicKey;
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
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
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
