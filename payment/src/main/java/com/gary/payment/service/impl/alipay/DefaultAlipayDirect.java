package com.gary.payment.service.impl.alipay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import com.gary.payment.PaymentException;
import com.gary.payment.account.alipay.AlipayDirectThirdAccount;
import com.gary.payment.service.AbstractRechargeService;
import com.gary.payment.service.RechargeService;
import com.gary.payment.util.alipay.AlipayNotify;
import com.gary.payment.util.alipay.AlipaySubmit;

/**
 * 支付宝WEB即时支付3.3
 * @ClassName: DefaultAlipayDirect 
 * @Description:  
 * @author zhouxianjun(Gary)
 * @date 2014-12-19 下午2:02:23 
 *
 */
public abstract class DefaultAlipayDirect extends AbstractRechargeService implements RechargeService {

	@Autowired(required = false)
	private AlipayDirectThirdAccount alipayDirectThirdAccount;
	
	public OrderRequest payment(PayRequest request) throws PaymentException {
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", alipayDirectThirdAccount.getPartner());
        sParaTemp.put("_input_charset", alipayDirectThirdAccount.getInput_charset());
		sParaTemp.put("payment_type", "1");
		sParaTemp.put("notify_url", alipayDirectThirdAccount.getReturnUrl());
		sParaTemp.put("return_url", alipayDirectThirdAccount.getReturnUrl());
		sParaTemp.put("seller_email", alipayDirectThirdAccount.getThirdAccount());
		sParaTemp.put("out_trade_no", request.getOrderNo());
		sParaTemp.put("subject", request.getGoods());
		sParaTemp.put("total_fee", request.getAmount().toString());
		Object showUrl = request.getData().get("show_url");
		if(showUrl != null)
			sParaTemp.put("show_url", showUrl.toString());
		Object body = request.getData().get("body");
		if(body != null)
			sParaTemp.put("body", body.toString());
		
		if(request.getData().containsKey("qr_pay_mode")){
			sParaTemp.put("qr_pay_mode", (String)request.getData().get("qr_pay_mode"));
		}
		
		sParaTemp = AlipaySubmit.buildRequestPara(sParaTemp);
		
		logger.info("支付宝即时支付请求参数:{}", sParaTemp);
		
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderNo(request.getOrderNo());
		orderRequest.setParamMap(sParaTemp);
		orderRequest.setPayUrl(alipayDirectThirdAccount.getPayUrl() + "?_input_charset=" + alipayDirectThirdAccount.getInput_charset());
		return orderRequest;
	}

	public PayResponse response(String type, Map<String, String> response) {
		logger.info("返回支付宝即时支付信息:{}", response);
		String status = response.get("trade_status");
		String out_trade_no = response.get("out_trade_no");
		String trade_no = response.get("trade_no");
		String amount = response.get("total_fee");
		BigDecimal successAmount = BigDecimal.valueOf(Double.valueOf(amount));
		boolean verify = AlipayNotify.verify(response);
		logger.info("支付宝支付异步返回验证签名:{}", verify);
		PayResponse payResponse = new PayResponse();
		payResponse.setOrderNo(out_trade_no);
		payResponse.setThirdOrderNo(trade_no);
		payResponse.setFee(fee());
		if(verify){
			boolean paySuccess = "TRADE_FINISHED".equals(status) || "TRADE_SUCCESS".equals(status);
			logger.info("收到支付宝即时支付结果通知信息：订单号：[{}],支付结果[{}],", out_trade_no, paySuccess);
			payResponse.setAmount(successAmount);
			payResponse.setResult("success");
			payResponse.setSuccess(paySuccess);
			return payResponse;
		}
		payResponse.setSuccess(false);
		payResponse.setResult("fail");
		return payResponse;
	}
}
