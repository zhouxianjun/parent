package com.gary.web.result;

import com.gary.util.ErrorsUtil;
import com.gary.web.util.I18nUtil;

import java.io.Serializable;
import java.util.Locale;

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
		if (resultMsg == null){
			String msg = I18nUtil.getMessage(String.valueOf(this.result), locale);
			return msg == null ? ErrorsUtil.getErrorDesc(this.result) : msg;
		}
		return this.resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
}
