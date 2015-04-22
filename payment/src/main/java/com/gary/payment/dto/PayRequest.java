package com.gary.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayRequest implements Serializable{

	private static final long serialVersionUID = -8309955100919640993L;
	/**充值用户唯一标识*/
	private String uid;
	/**订单号*/
	private String orderNo;
	/**用哪种充值*/
	private String type;
	/**充值金额*/
	private BigDecimal amount;
	/**充值到账金额*/
	private BigDecimal amountArrive;
	/**充值费率*/
	private BigDecimal fee;
	/**创建时间*/
	private Date createDate;
	/**商品*/
	private String goods;
	/**其它数据*/
	private Map<String, Object> data = new HashMap<String, Object>();
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmountArrive() {
		if(amountArrive == null && amount != null)
			this.amountArrive = amount.multiply(getFee());
		return amountArrive;
	}
	public void setAmountArrive(BigDecimal amountArrive) {
		this.amountArrive = amountArrive;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}

	@Override
	public String toString() {
		return "PayRequest{" +
				"uid='" + uid + '\'' +
				", orderNo='" + orderNo + '\'' +
				", type='" + type + '\'' +
				", amount=" + amount +
				", amountArrive=" + amountArrive +
				", fee=" + fee +
				", createDate=" + createDate +
				", goods='" + goods + '\'' +
				", data=" + data +
				'}';
	}
}
