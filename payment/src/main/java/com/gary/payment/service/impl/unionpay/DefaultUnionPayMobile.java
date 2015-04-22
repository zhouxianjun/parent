package com.gary.payment.service.impl.unionpay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import com.gary.payment.PaymentException;
import com.gary.payment.account.unionpay.UnionPayMobileThirdAccount;
import com.gary.payment.service.AbstractRechargeService;
import com.gary.payment.service.RechargeService;
import com.gary.payment.util.unionpay.UpmpConfig;
import com.gary.payment.util.unionpay.UpmpService;

/**
 * 银联手机支付
 * @ClassName: DefaultUnionPayMobile 
 * @Description:  
 * @author zhouxianjun(Gary)
 * @date 2014-12-19 下午2:07:28 
 *
 */
public abstract class DefaultUnionPayMobile extends AbstractRechargeService implements RechargeService {

	@Autowired(required = false)
	private UnionPayMobileThirdAccount unionPayMobileThirdAccount;
	
	public OrderRequest payment(PayRequest request) throws PaymentException {
		// 请求要素
		Map<String, String> req = new HashMap<String, String>();
		req.put("version", unionPayMobileThirdAccount.getVersion());// 版本号
		req.put("charset", unionPayMobileThirdAccount.getCharset());// 字符编码
		req.put("transType", unionPayMobileThirdAccount.getTransType());// 交易类型
		req.put("merId", unionPayMobileThirdAccount.getMerId());// 商户代码
		req.put("backEndUrl", unionPayMobileThirdAccount.getReturnUrl());// 通知URL
		req.put("orderDescription", request.getGoods());// 订单描述(可选)
		req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 交易开始日期时间yyyyMMddHHmmss
		req.put("orderNumber", request.getOrderNo());//订单号(商户根据自己需要生成订单号)
		req.put("orderAmount", parseAmount(request.getAmount()));// 订单金额
        req.put("orderCurrency", unionPayMobileThirdAccount.getCurrency());// 交易币种(可选)
        
        logger.info("银联手机支付参数:{}", req);
        Map<String, String> resp = new HashMap<String, String>();
		boolean validResp = UpmpService.trade(req, resp);
		String respCode = resp.get("respCode");
		logger.info("银联手机充值提交订单验证签名:{}", validResp);
		if(validResp && UpmpConfig.RESPONSE_CODE_SUCCESS.equals(respCode)){
			logger.info("银联手机充值下单结果:{}", resp);
			OrderRequest orderRequest = new OrderRequest();
			orderRequest.setOrderNo(request.getOrderNo());
			orderRequest.setParamMap(resp);
			return orderRequest;
		}
		throw new PaymentException("签名验证失败");
	}

	public PayResponse response(String type, Map<String, String> response) {
		logger.info("返回银联手机支付信息:{}", response);
		String orderNumber = response.get("orderNumber");
		String thirdPayNo = response.get("qn");
		String transStatus = response.get("transStatus");// 交易状态
		String respMsg = response.get("respMsg");
		String respCode = response.get("respCode");
		
		PayResponse payResponse = new PayResponse();
		payResponse.setOrderNo(orderNumber);
		payResponse.setAmount(parseAmount(response.get("settleAmount")));
		payResponse.setFee(fee());
		boolean verify = UpmpService.verifySignature(response);
		logger.info("银联手机支付结果验证签名:{};返回码:{}返回信息:{}", new Object[]{verify, respCode, respMsg});
		if(verify && UpmpConfig.RESPONSE_CODE_SUCCESS.equals(transStatus)){
			logger.info("银联手机充值结果:充值订单号{},流水号:{},返回状态:{}", new Object[] { orderNumber, thirdPayNo, transStatus});
			payResponse.setSuccess(true);
			return payResponse;
		}
		payResponse.setSuccess(false);
		payResponse.setErrorMsg("请求异常");
		return payResponse;
	}
	
	private String parseAmount(BigDecimal amount){
		return String.format("%.2f", amount).replace(".", "");
	}
	
	private BigDecimal parseAmount(String amount){
		return new BigDecimal(amount).divide(BigDecimal.valueOf(100));
	}
}
