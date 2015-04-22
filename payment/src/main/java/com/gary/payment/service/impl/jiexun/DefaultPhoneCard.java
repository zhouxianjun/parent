package com.gary.payment.service.impl.jiexun;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import com.gary.payment.PaymentException;
import com.gary.payment.account.jiexun.PhoneCardThirdAccount;
import com.gary.payment.service.AbstractRechargeService;
import com.gary.payment.service.RechargeService;
import com.gary.payment.util.Utils;
import com.gary.payment.util.jiexun.Des;
import com.gary.payment.util.payeco.common.SslConnection;

/**
 * 捷讯手机卡充值(data参数必须含有cardNo,password,cardType(CMJFK/LTJFK/DXJFK))
 * @ClassName: DefaultPhoneCard 
 * @Description:  
 * @author zhouxianjun(Gary)
 * @date 2014-12-19 下午3:30:31 
 *
 */
public abstract class DefaultPhoneCard extends AbstractRechargeService implements RechargeService {

	private static Map<String, String> phoneCardTypes;
	
	static{
		phoneCardTypes = new HashMap<String, String>();
		phoneCardTypes.put("CMJFK", "CMJFK00010001"); //移动
		phoneCardTypes.put("LTJFK", "LTJFK00020000"); //联通
		phoneCardTypes.put("DXJFK", "DXJFK00010001"); //电信
	}
	
	@Autowired
	private PhoneCardThirdAccount phoneCardThirdAccount;
	
	public OrderRequest payment(PayRequest request) throws PaymentException {
		String cardNo = (String)request.getData().get("cardNo");
		String password = (String)request.getData().get("password");
		String cardType = (String)request.getData().get("cardType");
		if(!phoneCardTypes.containsKey(cardType)){
			throw new PaymentException("手机卡类型错误");
		}
		if(!checkCardNo(cardType, cardNo)){
			throw new PaymentException("卡号无效");
		}
		if(!checkPassword(cardType, password)){
			throw new PaymentException("密码无效");
		}
		try {
			cardNo = Des.encryptData(cardNo, phoneCardThirdAccount.getMd5Key());
			password = Des.encryptData(password, phoneCardThirdAccount.getMd5Key());
		} catch (Exception e) {
			throw new PaymentException("充值加密数据发生异常");
		}
		
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("version_id", phoneCardThirdAccount.getVersion());
		params.put("merchant_id", phoneCardThirdAccount.getMerId());
		params.put("order_date", Utils.toYYYYMMDD(new Date()));
		params.put("order_id", request.getOrderNo());
		params.put("amount", request.getAmount().toString());
		params.put("currency", phoneCardThirdAccount.getCurrency());
		params.put("cardnum1", cardNo);
		params.put("cardnum2", password);
		params.put("pm_id", cardType);
		params.put("pc_id", phoneCardTypes.get(cardType));
		params.put("merchant_key", phoneCardThirdAccount.getMd5Key());
		
		String verifystring = Utils.MD5(mapToUrl(params));
		params.put("verifystring", verifystring);
		params.put("returl", "");
		params.put("notify_url", phoneCardThirdAccount.getReturnUrl());
		params.put("retmode", phoneCardThirdAccount.getRetMode());
		params.put("order_pdesc", "");
		params.put("user_name", "");
		params.put("user_phone", "");
		params.put("user_mobile", "");
		params.put("user_email", "");
		
		logger.info("手机卡支付请求参数:{}", params);
		SslConnection ssl = new SslConnection();
		try {
			String result = ssl.connect(phoneCardThirdAccount.getPayUrl() + "?" + mapToUrl(params), "UTF-8");
			logger.info("手机卡支付SSL结果:{}", result);
			String[] rets = result.split("\\|");
			String ret = rets[11];
			String resultMsg = rets[12];
			logger.info("手机卡支付下单返回结果 提示消息内容:{}", resultMsg);
			if (!ret.equals("P")) {
				throw new PaymentException("充值请求失败");
			}
		} catch (Exception e) {
			throw new PaymentException(e.getMessage());
		}
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderNo(request.getOrderNo());
		return orderRequest;
	}

	public PayResponse response(String type, Map<String, String> response) {
		logger.info("返回手机卡支付信息:{}", response);
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("version_id", response.get("version_id"));
		params.put("merchant_id", response.get("merchant_id"));
		params.put("order_date", response.get("order_date"));
		params.put("order_id", response.get("order_id"));
		params.put("currency", response.get("currency"));
		params.put("pay_sq", response.get("pay_sq"));
		params.put("pay_date", response.get("pay_date"));
		params.put("card_num", response.get("card_num"));
		params.put("card_pwd", response.get("card_pwd"));
		params.put("pc_id", response.get("pc_id"));
		params.put("card_status", response.get("card_status"));
		params.put("card_code", response.get("card_code"));
		params.put("card_amount", response.get("card_amount"));
		params.put("merchant_key", phoneCardThirdAccount.getMd5Key());
		
		PayResponse payResponse = new PayResponse();
		payResponse.setAmount(new BigDecimal(response.get("card_amount")));
		payResponse.setFee(fee());
		payResponse.setOrderNo(response.get("order_id"));
		payResponse.setThirdOrderNo(response.get("pay_sq"));
		String verifystring = Utils.MD5(mapToUrl(params));
		if(verifystring.equals(response.get("verifystring")) && "0".equals(response.get("card_status"))){
			logger.info("手机卡支付成功:充值订单号{},充值返回值{}", new Object[] { response.get("order_id"), response.get("card_status")});
			payResponse.setSuccess(true);
			payResponse.setResult("Y");
			return payResponse;
		}
		logger.info("手机卡支付失败,订单号:{}", response.get("order_id"));
		payResponse.setSuccess(false);
		payResponse.setResult("N");
		payResponse.setErrorMsg("支付失败,错误码:" + response.get("card_code"));
		return payResponse;
	}
	
	public String mapToUrl(LinkedHashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (isFirst) {
                sb.append(key).append("=").append(value);
                isFirst = false;
            } else {
                if (value != null) {
                	sb.append("&").append(key).append("=").append(value);
                }
            }
        }
        return sb.toString();
    }
	
	/**
	 * 校验充值卡卡号
	 * 移动:卡号17位密码18位
	 * 联通:卡号15位密码19位
	 * 电信:卡号19位密码18位
	 * @param type
	 * @param cardNo
	 * @return
	 * @return boolean
	 */
	private boolean checkCardNo(String type, String cardNo){
		if("CMJFK".equals(type)){
			return Utils.isNumeric(cardNo) && cardNo.length() == 17;
		}else if("LTJFK".equals(type)){
			return Utils.isNumeric(cardNo) && cardNo.length() == 15;
		}else if("DXJFK".equals(type)){
			return Utils.isNumeric(cardNo) && cardNo.length() == 19;
		}
		return false;
	}
	/**
	 * 校验充值卡密码
	 * 移动:卡号17位密码18位
	 * 联通:卡号15位密码19位
	 * 电信:卡号19位密码18位
	 * @param type
	 * @param password
	 * @return
	 * @return boolean
	 */
	private boolean checkPassword(String type, String password){
		if("CMJFK".equals(type)){
			return Utils.isNumeric(password) && password.length() == 18;
		}else if("LTJFK".equals(type)){
			return Utils.isNumeric(password) && password.length() == 19;
		}else if("DXJFK".equals(type)){
			return Utils.isNumeric(password) && password.length() == 18;
		}
		return false;
	}
}
