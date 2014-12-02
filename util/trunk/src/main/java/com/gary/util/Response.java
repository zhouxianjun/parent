package com.gary.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Response {
	private Logger logger = Logger.getLogger(Response.class);
	private HttpURLConnection urlConn;
	private String defenc = "UTF-8";
	public Response(HttpURLConnection urlConn) {
		this.urlConn = urlConn;
	}
	public String getHeaderValue(String key){
		return urlConn.getHeaderField(key);
	}
	public Map<String, List<String>> getHeader(){
		return urlConn.getHeaderFields();
	}
	public int getCode(){
		try {
			return urlConn.getResponseCode();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
	}
	private InputStream getIo(){
		InputStream io = null;
		try {
			io = urlConn.getInputStream();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return io;
	}
	public byte[] getData(){
		try {
			return Utils.input2byte(getIo());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public <T> T get(Class<T> c){
		if(InputStream.class.equals(c))
			return (T)getIo();
		else if(String.class.equals(c))
			return (T)getString(false, defenc);
		else if(Integer.class.equals(c) || Double.class.equals(c) || Float.class.equals(c) || Long.class.equals(c))
			return (T)getString(true, defenc);
		else if(JSONObject.class.equals(c))
			return (T)JSONObject.fromObject(getString(true, defenc));
		else if(JSONArray.class.equals(c))
			return (T)JSONArray.fromObject(getString(true, defenc));
		else{
			String result = getString(true, defenc);
			if(result.startsWith("{") && result.endsWith("}"))
				return (T)JSONObject.toBean(JSONObject.fromObject(result), c);
			else if(result.startsWith("[") && result.endsWith("]") && List.class.equals(c))
				return (T)JSONArray.toList(JSONArray.fromObject(result));
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public <T> T get(String enc){
		return (T)getString(false, enc);
	}
	@SuppressWarnings("unchecked")
	public <T> T getJSONObject(String enc){
		return (T)JSONObject.fromObject(get(enc));
	}
	@SuppressWarnings("unchecked")
	public <T> T getJSONArray(String enc){
		return (T)JSONArray.fromObject(get(enc));
	}
	private String getString(boolean isTrim, String enc){
		String str = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), enc));
			while ((str = br.readLine()) != null) {
				if(isTrim)
					sb.append(str.trim());
				else
					sb.append(str);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				urlConn.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		return getString(true, "UTF-8");
	}
}
