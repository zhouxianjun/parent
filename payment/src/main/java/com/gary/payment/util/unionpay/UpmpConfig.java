package com.gary.payment.util.unionpay;

public class UpmpConfig {
	// 版本号
	public static String VERSION;

	// 编码方式
	public static String CHARSET;
	
	// 交易网址
	public static String TRADE_URL;
	
	// 查询网址
	public static String QUERY_URL;

	// 商户代码
	public static String MER_ID;

	// 通知URL
	public static String MER_BACK_END_URL;
	
	// 前台通知URL
	public static String MER_FRONT_END_URL;
	
	// 返回URL
	public static String MER_FRONT_RETURN_URL;
	
	// 加密方式
	public static String SIGN_TYPE;

	// 商城密匙
	public static String SECURITY_KEY;
	
	// 成功应答码
	public static final String RESPONSE_CODE_SUCCESS = "00";

	// 签名
	public final static String SIGNATURE = "signature";
	
	// 签名方法
	public final static String SIGN_METHOD = "signMethod";
	
	// 应答码
	public final static String RESPONSE_CODE = "respCode";
	
	// 应答信息
	public final static String RESPONSE_MSG = "respMsg";
}
