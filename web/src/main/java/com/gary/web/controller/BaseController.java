package com.gary.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.xml.XmlMapper;
import com.gary.web.config.ApplicationContextHolder;
import com.gary.web.result.Result;

@Component
public abstract class BaseController extends com.gary.web.exception.ExceptionHandler {
	public Logger logger = LoggerFactory.getLogger(getClass());
	public static String JSON_TYPE = "application/json;charset=utf-8";
	public static String XML_TYPE = "application/json;charset=utf-8";
	public static String ERROR_RESULT_TYPE = "ERROR_RESULT_TYPE";
	public static String ERROR_RESULT_JSON = "JSON";
	public static String ERROR_RESULT_JSONP = "JSONP";
	public static String ERROR_RESULT_XML = "XML";
	private LocaleResolver localeResolver;
	private static ObjectMapper objectMapper;
	@Autowired
	private XmlMapper xmlMapper;
	@Autowired
	private HashMap<String, Integer> exceptionMap;
	@ExceptionHandler
	public void exception(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception{
		logger.error(ex.getMessage(), ex);
		Result result = exceptionHandler(ex, exceptionMap, getLocale(request));
		Object type = getResultType(request);
		writeData(request, response, transformResult(result, request), (String)type); 
	}
	protected Object transformResult(Result result, HttpServletRequest request){
		return result;
	}
	protected Object getResultType(HttpServletRequest request) {
		Object type = request.getAttribute(ERROR_RESULT_TYPE);
		if(type == null){
			type = request.getParameter(ERROR_RESULT_TYPE);
		}
		return type;
	}
	
	public static void writer(Object obj, String jsoncallback, HttpServletResponse response) throws Exception{
        if(StringUtils.isBlank(jsoncallback)){
            writeJSON(obj, response);
            return;
        }
        writeJSONP(obj, jsoncallback, response);
    }
	
	protected void writeData(HttpServletRequest request, HttpServletResponse response, Object result, String type) throws Exception{
		if(type == null)
			type = ERROR_RESULT_JSON;
		if(ERROR_RESULT_JSON.equalsIgnoreCase(type)){
			writeJSON(result, response);
		}else if(ERROR_RESULT_JSONP.equalsIgnoreCase(type)){
			String jsoncallback = request.getParameter("jsoncallback");
			if(jsoncallback == null){
				jsoncallback = "jsonp";
			}
			writeJSONP(result, jsoncallback, response);
		}else if(ERROR_RESULT_XML.equalsIgnoreCase(type)){
			writeXML(result, response);
		}else{
			request.setAttribute(ERROR_RESULT_TYPE, result);
			request.getRequestDispatcher(type).forward(request, response);
		}
	}
	public ServletContext getServletContext(HttpServletRequest request){
		return request.getSession().getServletContext();
	}
	public Locale getLocale(HttpServletRequest request){
		Locale locale = (Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(locale == null){
			if(localeResolver == null)
				localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver != null) {
				locale = localeResolver.resolveLocale(request);
			}else
				locale = null;
		}
		return locale;
	}
	public static void writeJSONP(Object obj, String jsoncallback, HttpServletResponse response) throws Exception{
		writer(jsoncallback + "(" + getObjectMapper().writeValueAsString(obj) + ")", response, JSON_TYPE);
	}
	private static void writer(String string,
			HttpServletResponse response, String type) throws IOException, Exception {
		response.setContentType(type);
		PrintWriter writer = response.getWriter();
		writer.print(string);
		writer.flush();
		writer.close();
	}
	public void writer(byte[] data,
			HttpServletResponse response) throws IOException, Exception {
		response.setContentType("application/x-download");
		response.setContentLength(data.length);
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
	}
	public static void writeJSON(Object obj, HttpServletResponse response) throws Exception{
		writer(getObjectMapper().writeValueAsString(obj), response, JSON_TYPE);
	}
	public void writeJSONObject(Object obj, HttpServletResponse response) throws Exception{
		writer(JSONObject.fromObject(obj).toString(), response, JSON_TYPE);
	}
	public void writeJSONArray(Object obj, HttpServletResponse response) throws Exception{
		writer(JSONArray.fromObject(obj).toString(), response, JSON_TYPE);
	}
	public void writeXML(Object obj, HttpServletResponse response) throws Exception{
		writer(xmlMapper.writeValueAsString(obj), response, XML_TYPE);
	}
	public void writeXML(JSON json, HttpServletResponse response) throws Exception{
		XMLSerializer xml = new XMLSerializer();
		writer(xml.write(json), response, XML_TYPE);
	}
	
	protected String getPath(HttpServletRequest request, String path){
		return request.getSession().getServletContext().getRealPath("/") + File.separator + (path == null ? "" : path) + File.separator;
	}
	
	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	protected String getIpAddr(HttpServletRequest request) {
		String remoteIp = request.getHeader("x-forwarded-for");
		if (remoteIp == null || remoteIp.isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp = request.getHeader("X-Real-IP");
		}
		if (remoteIp == null || remoteIp.isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp = request.getHeader("Proxy-Client-IP");
		}
		if (remoteIp == null || remoteIp.isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (remoteIp == null || remoteIp.isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp = request.getHeader("HTTP_CLIENT_IP");
		}
		if (remoteIp == null || remoteIp.isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (remoteIp == null || remoteIp.isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp = request.getRemoteAddr();
		}
		if (remoteIp == null || remoteIp.isEmpty()
				|| "unknown".equalsIgnoreCase(remoteIp)) {
			remoteIp = request.getRemoteHost();
		}
		return remoteIp;
	}
	
	public HttpSession getSession(HttpServletRequest request){
		return request.getSession();
	}
	public static void setObjectMapper(ObjectMapper objectMapper) {
		BaseController.objectMapper = objectMapper;
	}
	public static ObjectMapper getObjectMapper() {
		objectMapper = objectMapper == null ? ApplicationContextHolder.getBean("objectMapper", ObjectMapper.class) : objectMapper;
		return objectMapper;
	}
}
