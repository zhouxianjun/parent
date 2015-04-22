package com.gary.payment.account.unionpay;

public class UnionPayUPOPThirdAccount {
	/**商户号*/
	private String MerId;
	/**交易类型：0001支付*/
	private String TransType = "0001";
	/**交易币种：156RMB*/
	private String CuryId = "156";
	/**接入版本号V4*/
	private String Version = "20070129";
	/**为后台接受应答地址，用于商户记录交易信息和处理,URL不能包含参数，如果需要填写参数，可把参数填写在“商户私有域”Priv1字段中*/
	private String BgRetUrl;
	/**为页面接受应答地址，用于引导使用者返回支付后的商户网站页面*/
	private String PageRetUrl;
	/**提交到银联支付地址*/
	private String payUrl;
	/**私钥KEY地址*/
	private String privateKey;
	/**公钥KEY地址*/
	private String publicKey;
	public String getMerId() {
		return MerId;
	}
	public void setMerId(String merId) {
		MerId = merId;
	}
	public String getTransType() {
		return TransType;
	}
	public void setTransType(String transType) {
		TransType = transType;
	}
	public String getCuryId() {
		return CuryId;
	}
	public void setCuryId(String curyId) {
		CuryId = curyId;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getBgRetUrl() {
		return BgRetUrl;
	}
	public void setBgRetUrl(String bgRetUrl) {
		BgRetUrl = bgRetUrl;
	}
	public String getPageRetUrl() {
		return PageRetUrl;
	}
	public void setPageRetUrl(String pageRetUrl) {
		PageRetUrl = pageRetUrl;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
