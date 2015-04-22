package com.gary.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gary.error.ErrorCode;
import com.gary.web.result.ExecuteResult;
import com.gary.web.result.Result;

@Controller
@RequestMapping("exception")
public class ExceptionController extends BaseController {
	@RequestMapping("404")
	public void notFount(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception{
		write(request, response, ErrorCode.NOT_FOUND, null);
	}
	@RequestMapping("500")
	public void serverError(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception{
		exception(request, response, e);
	}
	@RequestMapping("userfailure")
	public void userFailure(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception{
		write(request, response, ErrorCode.UN_AUTHORIZED, "用户认证失败!");
	}
	@RequestMapping("error")
	public void error(HttpServletRequest request, HttpServletResponse response, Exception e, @RequestParam int code) throws Exception{
		write(request, response, code, null);
	}
	@SuppressWarnings("deprecation")
	private void write(HttpServletRequest request,
			HttpServletResponse response, int code, String msg) throws Exception {
		Exception e;
		Result result = new Result();
		result.setExecuteResult(new ExecuteResult(code));
		result.setMsg(msg);
		Object exception = request.getAttribute("exception");
		Object res = request.getAttribute(ERROR_RESULT_TYPE);
		if(res == null){
			res = request.getParameter(ERROR_RESULT_TYPE);
		}
		if(exception != null){
			e = (Exception)exception;
			if(e instanceof org.springframework.security.core.AuthenticationException){
				org.springframework.security.core.AuthenticationException ea = (org.springframework.security.core.AuthenticationException)e;
				org.springframework.security.core.Authentication auth = ea.getAuthentication();
				result.getData().put("authentication", auth);
				e = ea;
			}
			result.setMsg(result.getMsg() == null ? "" : result.getMsg() + e.getMessage());
		}
		writeData(request, response, transformResult(result, request), (String)res);
	}
}
