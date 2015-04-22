package com.gary.payment;

import java.math.BigDecimal;
import java.util.Map;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gary.payment.service.RechargeService;

@Component
public class RechargeManager implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	public OrderRequest pay(String type, BigDecimal amount, String uid, String goods, Map<String, Object> data) throws PaymentException{
		if(applicationContext.containsBean(type)){
			RechargeService service = applicationContext.getBean(type, RechargeService.class);
			PayRequest request = service.createPayRequest(uid, amount, type, goods);
			if(request == null){
				throw new PaymentException(type + " 类型充值下单失败!");
			}
			if(data != null)
				request.setData(data);
			if(!service.saveRequest(request)){
				throw new PaymentException(type + " 类型充值下单失败!");
			}
			return service.payment(request);
		}
		throw new PaymentException(type + " 类型充值未实现!");
	}
	
	public PayResponse response(String type, Map<String, String> request) throws PaymentException{
		if(applicationContext.containsBean(type)){
			RechargeService service = applicationContext.getBean(type, RechargeService.class);
			PayResponse response = service.response(type, request);
			service.afterResponse(type, request, response);
			return response;
		}
		throw new PaymentException(type + " 类型充值未实现!");
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		if(applicationContext == null)
			applicationContext = arg0;
	}
}
