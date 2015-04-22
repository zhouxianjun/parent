package com.gary.payment.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderStore implements Serializable {
	private static final long serialVersionUID = -7707480190387554477L;
	private AtomicInteger sqe = new AtomicInteger(1);
	private Date updateDate = new Date();
	public AtomicInteger getSqe() {
		return sqe;
	}
	public void setSqe(AtomicInteger sqe) {
		this.sqe = sqe;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
