package com.gary.payment.account.unionpay;

import org.springframework.beans.factory.InitializingBean;

import com.gary.payment.util.unionpay.UpmpConfig;

public class UnionPayMobileThirdAccount implements InitializingBean {
	/**版本号 目前1.0.0*/
	private String version = "1.0.0";
	/**编码方式*/
	private String charset = "UTF-8";
	/**请求第三方地址*/
	private String requestUrl;
	/**商户号*/
	private String merId;
	/**异步通知地址*/
	private String returnUrl;
	/**加密方式 默认MD5*/
	private String signType = "MD5";
	/**商户密钥*/
	private String securityKey;
	/**交易类型：01支付*/
	private String transType = "01";
	/**交易币种：156RMB*/
	private String currency = "156";
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSecurityKey() {
		return securityKey;
	}
	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void afterPropertiesSet() throws Exception {
		UpmpConfig.CHARSET = this.charset;
		UpmpConfig.MER_BACK_END_URL = this.returnUrl;
		UpmpConfig.MER_ID = this.merId;
		UpmpConfig.SECURITY_KEY = this.securityKey;
		UpmpConfig.SIGN_TYPE = this.signType;
		UpmpConfig.TRADE_URL = this.requestUrl;
		UpmpConfig.VERSION = this.version;
	}
}
