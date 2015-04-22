package com.gary.web.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.gary.web.exception.error.ErrorCode;

public class Result implements Serializable {
	private static final long serialVersionUID = 816334571679000487L;
	/**当前操作是否成功*/
	private Boolean success;
	/**返回文本信息*/
	private String msg;
	/**附加信息*/
	private Map<String, Object> data;
	/**错误代码及错误信息*/
	private ExecuteResult executeResult = new ExecuteResult(ErrorCode.SUCCESS);

	public Result() {
		// TODO Auto-generated constructor stub
	}
	
	public Result(ExecuteResult executeResult) {
		this.executeResult = executeResult;
	}
	
	public Result(ExecuteResult executeResult, String msg) {
		this.executeResult = executeResult;
		this.msg = msg;
	}
	
	public Boolean isSuccess() {
		if(success == null)
			success = StringUtils.isNumeric(executeResult.getResult().toString()) && executeResult.getResult().toString().equals(String.valueOf(ErrorCode.SUCCESS));
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg == null ? getExecuteResult().getResultMsg() : msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		if(data == null)
			data = new HashMap<String, Object>();
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public Result put(String key, Object value){
		getData().put(key, value);
		return this;
	}
	
	public Result putAll(Map<String, Object> data){
		getData().putAll(data);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public <T> T get(String key){
		return (T) getData().get(key);
	}

	public ExecuteResult getExecuteResult() {
		if(executeResult == null)
			executeResult = new ExecuteResult(ErrorCode.SUCCESS);
		return executeResult;
	}

	public void setExecuteResult(ExecuteResult executeResult) {
		this.executeResult = executeResult;
	}
}
