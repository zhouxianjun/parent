package com.gary.payment.service.impl.unionpay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import chinapay.PrivateKey;
import chinapay.SecureLink;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import com.gary.payment.PaymentException;
import com.gary.payment.account.unionpay.UnionPayThirdAccount;
import com.gary.payment.service.AbstractRechargeService;
import com.gary.payment.service.RechargeService;
import com.gary.payment.util.Utils;

/**
 * 银联银行卡支付
 * @ClassName: DefaultUnionPay 
 * @Description:  
 * @author zhouxianjun(Gary)
 * @date 2014-12-19 下午2:07:12 
 *
 */
public abstract class DefaultUnionPay extends AbstractRechargeService implements RechargeService {
	@Autowired(required = false)
	protected UnionPayThirdAccount unionPayThirdAccount;
	
	public OrderRequest payment(PayRequest request) throws PaymentException {
		String money = Utils.parseMoney(request.getAmount(), 12);
		String gateId = (String)request.getData().get("gateId");
		if(unionPayThirdAccount.isDebug()){
			gateId = "";
		}
		String merId = unionPayThirdAccount.getMerId();
		String orderId = request.getOrderNo();
		String curyId = unionPayThirdAccount.getCuryId();
		String transDate = Utils.toYYYYMMDD(new Date());
		String transType = unionPayThirdAccount.getTransType();
		//封装银联支付的参数
		Map<String, String> parmMap = new HashMap<String, String>();
		parmMap.put("MerId", merId);
		parmMap.put("OrdId", orderId);
		parmMap.put("TransAmt", money); //订单交易金额，12位长度，左补0,单位为分
		parmMap.put("TransDate", transDate); //订单交易日期，8位长度
		parmMap.put("TransType", transType); //交易类型：0001支付
		parmMap.put("CpVersion", unionPayThirdAccount.getVersion()); //接入版本号V4
		parmMap.put("CuryId", curyId); //订单交易币种，3位长度，固定为人民币156
		parmMap.put("GateId", gateId); //支付网关号,测试不需要
		parmMap.put("PageRetUrl", unionPayThirdAccount.getPageRetUrl()); //页面交易接收URL，长度不要超过80个字节
		parmMap.put("BgRetUrl", unionPayThirdAccount.getBgRetUrl()); //后台交易接收URL，长度不要超过80个字节
		String priv1 = "";
		if(request.getData().get("priv1") != null){
			priv1 = (String)request.getData().get("priv1");
		}
		parmMap.put("Priv1", priv1); //私有自定义字段,银联原封不动回传
		String chk = new SecureLink(getPrivateKey(merId)).Sign(merId + orderId + money + curyId + transDate + transType + priv1);
		parmMap.put("ChkValue", chk); //256字节长的ASCII码,为此次交易提交关键数据的数字签名
		
		logger.info("银联支付请求参数:{}", parmMap);
		
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderNo(request.getOrderNo());
		orderRequest.setParamMap(parmMap);
		orderRequest.setPayUrl(unionPayThirdAccount.getPayUrl());
		return orderRequest;
	}

	public PayResponse response(String type, Map<String, String> response) {
		logger.info("返回银联在线信息:{}", response);
		//订单数据准备
		String MerId = response.get("merid");
		String OrdId = response.get("orderno");
		String TransAmt = response.get("amount");// 12
		String CuryId = response.get("currencycode");// 3
		String TransDate = response.get("transdate");// 8
		String TransType = response.get("transtype");// 4
		String Status = response.get("status");
		String GateId = response.get("GateId");
		String ChkValue = response.get("checkvalue");
		boolean verify = new SecureLink(getPrivateKey()).verifyTransResponse(MerId, OrdId, TransAmt, CuryId, TransDate, TransType, Status, ChkValue);
		
		BigDecimal parseMoney = Utils.parseMoney(TransAmt, 12);
		PayResponse payResponse = new PayResponse();
		payResponse.setOrderNo(OrdId);
		payResponse.setAmount(parseMoney);
		payResponse.setFee(fee());
		if(verify && "1001".equals(Status)){
			logger.info("银联在线充值结果:充值订单号{},充值返回值{},支付网关{}", new Object[] { OrdId, Status, GateId});
			payResponse.setSuccess(true);
			return payResponse;
		}
		
		payResponse.setSuccess(false);
		payResponse.setErrorMsg("支付失败");
		return payResponse;
	}

	private PrivateKey getPrivateKey(String MerId) {  
		try {
			boolean buildOK = false;
			PrivateKey key = new PrivateKey();
			String path = unionPayThirdAccount.getPrivateKey();
			buildOK = key.buildKey(MerId, 0, path);
			return buildOK ? key : null;
		} catch (Exception e) {
			logger.error("银联支付私钥KEY错误", e);
			return null;
		}
    }
	
	private PrivateKey getPrivateKey(){
		try {
			boolean buildOK = false;
			PrivateKey key = new PrivateKey();
			String path = unionPayThirdAccount.getPublicKey();
			buildOK = key.buildKey("999999999999999", 0, path);
			return buildOK ? key : null;
		} catch (Exception e) {
			logger.error("银联支付公钥KEY错误", e);
			return null;
		}
	}
}
