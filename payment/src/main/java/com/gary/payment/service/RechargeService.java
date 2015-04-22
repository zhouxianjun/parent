package com.gary.payment.service;

import java.math.BigDecimal;
import java.util.Map;

import com.gary.payment.dto.OrderRequest;
import com.gary.payment.dto.OrderStore;
import com.gary.payment.dto.PayRequest;
import com.gary.payment.dto.PayResponse;
import com.gary.payment.PaymentException;

public interface RechargeService {
	/**
	 * 执行支付请求
	 * @param request
	 * @return
	 * @throws PaymentException
	 */
	public OrderRequest payment(PayRequest request) throws PaymentException;

	/**
	 * 接收处理支付返回结果
	 * @param type 充值渠道类型
	 * @param response 返回的数据
	 * @return
	 */
	public PayResponse response(String type, Map<String, String> response);

	/**
	 * 处理支付返回结果后事件
	 * @param type 充值渠道类型
	 * @param response 返回的数据
	 * @param payResponse PayResponse
	 */
	public void afterResponse(String type, Map<String, String> response, PayResponse payResponse);

	/**
	 * 保存支付请求，返回false则下单失败，后续操作不再进行
	 * @param request
	 * @return
	 * @throws PaymentException
	 */
	public boolean saveRequest(PayRequest request) throws PaymentException;
	
	/**
	 * 获取订单序列号,当天超过1000则重置,每天从1开始
	 * @param orderStore 订单号数据
	 * @return int
	 */
	public int getNoIndex(OrderStore orderStore);
	
	/**
	 * 创建订单号
	 * @return String
	 */
	public String createOrderNo();

	/**
	 * 创建一个充值请求，返回null则下单失败，后续操作不再进行
	 * @param uid 充值用户唯一ID
	 * @param amount 充值金额
	 * @param type 充值渠道类型
	 * @param goods 商品描述
	 * @return
	 */
	public PayRequest createPayRequest(String uid, BigDecimal amount, String type, String goods);

	/**
	 * 判断用户是否已启用或禁止，默认为true
	 * @param uid
	 * @return
	 */
	public boolean isUserEnable(String uid);

	/**
	 * 最小充值金额
	 * @return
	 */
	public double minAmount();

	/**
	 * 最大充值金额
	 * @return
	 */
	public double maxAmount();

	/**
	 * 费率
	 * @return
	 */
	public BigDecimal fee();
}
