package com.gary.payment.service.impl.alipay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import com.gary.payment.PaymentException;
import com.gary.payment.account.alipay.AlipayMobileThirdAccount;
import com.gary.payment.service.AbstractRechargeService;
import com.gary.payment.service.RechargeService;
import com.gary.payment.util.alipay.AlipayNotify;
import com.gary.payment.util.alipay.RsaMobile;

/**
 * 支付宝手机支付3.3
 * @ClassName: DefaultAlipayMobile 
 * @Description:  
 * @author zhouxianjun(Gary)
 * @date 2014-12-19 上午10:20:19 
 *
 */
public abstract class DefaultAlipayMobile extends AbstractRechargeService implements RechargeService {

	/**
     * 支付宝消息验证地址
     */
    public static String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";
	
	@Autowired(required = false)
	private AlipayMobileThirdAccount alipayMobileThirdAccount;
	
	public OrderRequest payment(PayRequest request) throws PaymentException {
		//把请求参数打包成数组
		LinkedHashMap<String, String> sParaTemp = new LinkedHashMap<String, String>();
		sParaTemp.put("partner", alipayMobileThirdAccount.getPartner());
		sParaTemp.put("out_trade_no", request.getOrderNo());
		sParaTemp.put("subject", request.getGoods());
		Object body = request.getData().get("body");
		if(body != null)
			sParaTemp.put("body", body.toString());
		sParaTemp.put("total_fee", request.getAmount().toString());
		sParaTemp.put("notify_url", encode(alipayMobileThirdAccount.getReturnUrl()));
		sParaTemp.put("service", "mobile.securitypay.pay");
        sParaTemp.put("_input_charset", alipayMobileThirdAccount.getInput_charset());
		sParaTemp.put("payment_type", "1");
		sParaTemp.put("seller_id", alipayMobileThirdAccount.getThirdAccount());
		Object showUrl = request.getData().get("show_url");
		if(showUrl != null)
			sParaTemp.put("show_url", showUrl.toString());
		sParaTemp.put("it_b_pay", "30m");
		String sign = RsaMobile.sign(mapToUrl(sParaTemp), alipayMobileThirdAccount.getKey());
		sParaTemp.put("sign", encode(sign));
		sParaTemp.put("sign_type", "RSA");
		//payUrl返回给手机端直接使用
		sParaTemp.put("payUrl", mapToUrl(sParaTemp));
		
		logger.info("支付宝手机支付生成本地订单信息:{}", sParaTemp);
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderNo(request.getOrderNo());
		orderRequest.setParamMap(sParaTemp);
		return orderRequest;
	}

	public PayResponse response(String type, Map<String, String> response) {
		logger.info("返回支付宝手机支付信息:{}", response);
		String status = response.get("trade_status");
		String out_trade_no = response.get("out_trade_no");
		String trade_no = response.get("trade_no");
		String amount = response.get("total_fee");
		BigDecimal successAmount = BigDecimal.valueOf(Double.valueOf(amount));
		boolean verify = AlipayNotify.verify(response);
		logger.info("支付宝手机支付异步返回验证签名:{}", verify);
		PayResponse payResponse = new PayResponse();
		payResponse.setOrderNo(out_trade_no);
		payResponse.setThirdOrderNo(trade_no);
		payResponse.setFee(fee());
		if(verify){
			boolean paySuccess = "TRADE_FINISHED".equals(status) || "TRADE_SUCCESS".equals(status);
			logger.info("收到支付宝手机支付结果通知信息：订单号：[{}],支付结果[{}],", out_trade_no, paySuccess);
			payResponse.setAmount(successAmount);
			payResponse.setResult("success");
			payResponse.setSuccess(paySuccess);
			return payResponse;
		}
		payResponse.setSuccess(false);
		payResponse.setResult("fail");
		return payResponse;
	}
	
	public String mapToUrl(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (isFirst) {
                sb.append(key + "=\"" + value + "\"");
                isFirst = false;
            } else {
                if (value != null) {
                    sb.append("&" + key + "=\"" + value + "\"");
                }
            }
        }
        return sb.toString();
    }
	
	private String encode(String str){
		try {
			str = URLEncoder.encode(str, alipayMobileThirdAccount.getInput_charset());
		} catch (Exception e) {}
		return str;
	}
}
