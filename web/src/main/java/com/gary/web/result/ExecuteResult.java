package com.gary.web.result;

import java.io.Serializable;
import java.util.Locale;

import com.gary.web.util.ErrorsUtil;

public class ExecuteResult implements Serializable {
	private static final long serialVersionUID = -7503032276558725008L;
	private Object result;
	private String resultMsg;
	private Locale locale;
	public ExecuteResult(Object result) {
		this.result = result;
	}
	public ExecuteResult(Integer result,Locale locale) {
		this.result = result;
		this.locale = locale;
	}
	public ExecuteResult(Integer result, String resultMsg) {
		this.result = result;
		this.resultMsg = resultMsg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getResultMsg() {
		return resultMsg == null ? ErrorsUtil.getErrorDesc(this.result, locale) : this.resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
}
