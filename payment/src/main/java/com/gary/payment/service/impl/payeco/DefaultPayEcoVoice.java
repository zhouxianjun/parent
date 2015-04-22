package com.gary.payment.service.impl.payeco;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import com.gary.payment.PaymentException;
import com.gary.payment.account.payeco.PayEcoVoiceThirdAccount;
import com.gary.payment.service.AbstractRechargeService;
import com.gary.payment.service.RechargeService;
import com.gary.payment.util.payeco.client.TransactionClient;
import com.gary.payment.util.payeco.client.XmlV2;
import com.gary.payment.util.payeco.common.Formatter;

/**
 * 易联语音支付
 * @ClassName: DefaultPayEcoVoice 
 * @Description:  data参数必须含有(idCard,realName,bankAddr(以|分隔),bankCard,phone)
 * @author zhouxianjun(Gary)
 * @date 2014-12-19 下午2:06:57 
 *
 */
public abstract class DefaultPayEcoVoice extends AbstractRechargeService implements RechargeService {

	@Autowired(required = false)
	protected PayEcoVoiceThirdAccount payEcoVoiceThirdAccount;
	
	private static TransactionClient tc;
	private static String idCardType = "01"; //银行卡开户证件类型，参照字段类型(特殊需求商户填写) “01”	身份证 “02”	护照 “03”	军人证 “04”	台胞证
	private static String currency = "CNY"; //交易币种, CNY：人民币
	private static String accountType = "01"; //交易卡类型 “01“	借记卡号 “02”	信用卡号 “03”	预付卡
	private static String orderType = "00"; //订单类型 00表示即时支付(默认)；01表示非即时支付
	private static String orderFrom = "02"; //订单来源 “02”商户OrderNo，网页自助下单（WEB） “03”	商户OrderNo，客服电话下单（CallCenter） “04”	商户OrderNo，电话自助下单（IVR） “05”	商户OrderNo，手机自助下单（WAP）
	
	public OrderRequest payment(PayRequest request) throws PaymentException {
		String idCard = (String)request.getData().get("idCard");
		String realName = (String)request.getData().get("realName");
		String bankAddr = (String)request.getData().get("bankAddr");
		String bankCard = (String)request.getData().get("bankCard");
		String phone = (String)request.getData().get("phone");
		if(StringUtils.isEmpty(idCard) || StringUtils.isEmpty(realName) || StringUtils.isEmpty(bankAddr) || StringUtils.isEmpty(bankCard) || StringUtils.isEmpty(phone)){
			throw new PaymentException("用户信息不完善");
		}
		
		String amount = request.getAmount().toString();
		String orderId = request.getOrderNo();
		String goods = request.getGoods();
		String[] bankAddrs = bankAddr.split("\\|");
		if(bankAddrs.length != 2){
			throw new PaymentException("银行卡地址错误");
		}
		
		try {
			XmlV2 accq = getClient().accountQuery(bankCard, accountType, phone, amount, currency, Formatter.HHmmss(new Date()), 
					null, null, null, null,null, null, null, null, 
					null, null, null, null, null, null, null, null);
			logger.debug("风控校验返回信息: {}", accq);
			String respCode = accq.getRespCode();
			XmlV2 pay = null;
			if("0000".equals(respCode)){
				logger.info("易联语音支付,风控校验{}为白名单,直接支付!", respCode);
				pay = pay(goods, bankCard, phone, amount, orderId, realName);
			}else if("T437".equals(respCode) || "T438".equals(respCode)){
				//新用户或者灰用户
				logger.info("易联语音支付,风控校验{}为新用户或灰用户,需要认证支付: {}!", respCode, accq.getRiskFlags());
				pay = pay(goods, bankCard, phone, amount, orderId, realName, idCard, bankAddr);
			}else{
				logger.info("易联语音支付{},不支持的卡{},返回信息: {}", new Object[]{respCode, bankCard, accq.getRemark()});
				throw new PaymentException("系统不支持的用户!");
			}
			logger.debug("易联语音支付下单返回信息: {}", pay);
			respCode = pay.getRespCode();
			if("00A3".equals(respCode)){
				logger.info("易联语音支付下单成功{},等待回呼!", respCode);
				OrderRequest orderRequest = new OrderRequest();
				orderRequest.setOrderNo(request.getOrderNo());
				return orderRequest;
			}
			logger.info("易联语音支付下单失败{},返回信息: {}!", respCode, pay.getRemark());
			throw new PaymentException("支付失败!");
		} catch (Exception e) {
			logger.warn("易联语音支付,未知异常[" + orderId + "]bankCard[" + bankCard + "],message:"+e.getMessage(), e);
			throw new PaymentException(e.getMessage());
		}
	}
	
	public TransactionClient getClient(){
		if(tc != null)
			return tc;
		tc = new TransactionClient(payEcoVoiceThirdAccount.getSslUrl());
		tc.setMerchantNo(payEcoVoiceThirdAccount.getMerchantNo());
		tc.setMerchantPassWD(payEcoVoiceThirdAccount.getMerchantPasswd());
		tc.setTerminalNo(payEcoVoiceThirdAccount.getTermNo());
		return tc;
	}
	
	public XmlV2 pay(String goods, String cardNo, String phone, String amount, String order, String iDCardName, String iDCardNo, String bankAddress) throws Exception{
		//190000:即时订单 190001:非即时订单申请  190002:代收 190003:代付 190004:信用卡还款  190005:预授权  190006:预授权完成
		return tc.pay("190000", cardNo, accountType, phone, amount, currency, payEcoVoiceThirdAccount.getReturnUrl(), payEcoVoiceThirdAccount.getReturnUrl(), payEcoVoiceThirdAccount.getMerchantName(), 
				Formatter.HHmmss(new Date()), order, orderFrom, null, goods, orderType, null, iDCardName, iDCardNo, idCardType, 
				bankAddress, null, null, null, null, null, null, 
				null, null, null, null);
	}
	
	public XmlV2 pay(String goods, String cardNo, String phone, String amount, String order, String iDCardName) throws Exception{
		//190000:即时订单 190001:非即时订单申请  190002:代收 190003:代付 190004:信用卡还款  190005:预授权  190006:预授权完成
		return tc.pay("190000", cardNo, accountType, phone, amount, currency, payEcoVoiceThirdAccount.getReturnUrl(), payEcoVoiceThirdAccount.getReturnUrl(), payEcoVoiceThirdAccount.getMerchantName(), 
				Formatter.HHmmss(new Date()), order, orderFrom, null, goods, orderType, null, null, null, idCardType, 
				null, null, null, null, null, null, null, 
				null, null, null, null);
	}

	public PayResponse response(String type, Map<String, String> paramMap) {
		String responseText = paramMap.get("response_text");
		responseText = new String(Base64.decodeBase64(responseText));
		XmlV2 response = new XmlV2(responseText);
		logger.info("易联语音支付异步返回: {}", response);
		String accountNum = response.getAccountNo();
		String amount = response.getAmount();
		BigDecimal decimalAmount = BigDecimal.valueOf(Double.valueOf(amount));

		String respCode = response.getRespCode();
		String orderNo = response.getMerchantOrderNo();
		String thirdOrderNo = response.getOrderNo();
		logger.info("用户银行卡号：{} 金额：{} 订单号：{}-{} 易联返回支付状态：{}", new Object[]{accountNum, amount, orderNo, thirdOrderNo, respCode});
		
		String resMac = response.getMAC();
		String genMac = response.generateMac(payEcoVoiceThirdAccount.getMerchantPasswd());
		PayResponse payResponse = new PayResponse();
		payResponse.setOrderNo(orderNo);
		payResponse.setThirdOrderNo(thirdOrderNo);
		payResponse.setAmount(decimalAmount);
		payResponse.setFee(fee());
		if(!resMac.equals(genMac)){
			logger.info("易联语音支付异步返回失败,MAC校验错误!{}", resMac);
			payResponse.setErrorMsg("易联通知接收签名验证错误");
			payResponse.setSuccess(false);
			return payResponse;
		}
		payResponse.setResult("0000");
		if("0000".equals(respCode)){
			logger.info("易联语音支付异步返回{}成功,{}", respCode, response.getRemark());
			payResponse.setSuccess(true);
			return payResponse;
		}
		payResponse.setSuccess(false);
		logger.info("易联语音支付异步返回{}失败,{}", respCode, response.getRemark());
		return payResponse;
	}
}
