package com.gary.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gary.util.dto.Cookie;


public class HttpClient {
	private Logger logger = Logger.getLogger(HttpClient.class);
	private String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)";
	private static final String POST_PARAMETER_SPLITOR = "&";
	private static final String POST_PARAMETER_KV_SPLITOR = "=";
	private static final String BOUNDARY = "7dbea2a110230v";
	private static final String COOKIE_KEY = "Set-Cookie";
	private boolean isFilterEmpty;
	private boolean redirect = true;
	private String referer = null;
	private static HttpClient httpClient;
	private Map<String, List<Cookie>> cookies = new HashMap<String, List<Cookie>>();
	
	public static HttpClient getSingleton(){
		if(httpClient == null)
			httpClient = new HttpClient();
		return httpClient;
	}
	
	public void setRedirect(boolean redirect){
		this.redirect = redirect;
	}
	
	/**
	 * GET方式模拟用户访问
	 * @param url 网站入口
	 * @param param 参数
	 * @param enc 访问编码
	 * @return
	 */
	public Response getMethod(String url, Map<String,Object> param) {
		logger.debug("Reading GET: " + url);
		Response res = null;
		StringBuffer params = setParams(param);
		if(url.endsWith("?"))
			params.delete(0, 1);
		try {
			URL u = new URL(param == null ? url : url + params);
			HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
			urlConn.setInstanceFollowRedirects(false);
			if(referer != null)
				urlConn.addRequestProperty("Referer", referer);
			setCookies(urlConn);
			urlConn.setRequestProperty("User-Agent", USER_AGENT_VALUE); 
			saveCookies(urlConn);
			String location = urlConn.getHeaderField("Location");
			if((urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) && location != null && redirect){
				this.referer = location;
				logger.debug("Redirect: " + location);
				return getMethod(location);
			}
			res = new Response(urlConn);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return res;
	}

	private StringBuffer setParams(Map<String, Object> param) {
		StringBuffer params = null;
		if(param != null){
			params = new StringBuffer("?");
			int i = 0;
			for (String key : param.keySet()) {
				Object value = param.get(key);
				if(value == null){
					continue;
				}
				if(isFilterEmpty && "".equals(value))
					continue;
				if(value instanceof Object[]){
					Object[] vals = (Object[])value;
					for (Object string : vals) {
						params.append(i != 0 ? POST_PARAMETER_SPLITOR : "").append(key).append(POST_PARAMETER_KV_SPLITOR).append(string);
						logger.debug("参数: " + key + "=" + string);
					}
				}else{
					params.append(i != 0 ? POST_PARAMETER_SPLITOR : "");
					params.append(key);
					params.append(POST_PARAMETER_KV_SPLITOR);
					params.append(value);
					logger.debug("参数: " + key + "=" + value);
				}
				i++;
			}
		}
		return params;
	}
	/**
	 * GET方式模拟用户访问
	 * @param url 网站入口 + 参数
	 * @param enc 访问编码
	 * @return
	 */
	public Response getMethod(String url) {
		logger.debug("Reading GET: " + url);
		Response res = null;
		try {
			URL u = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
			urlConn.setInstanceFollowRedirects(false);
			if(referer != null)
				urlConn.addRequestProperty("Referer", referer);
			setCookies(urlConn);
			urlConn.setRequestProperty("User-Agent", USER_AGENT_VALUE); 
			saveCookies(urlConn);
			String location = urlConn.getHeaderField("Location");
			if((urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) && location != null && redirect){
				this.referer = location;
				logger.debug("Redirect: " + location);
				return getMethod(location);
			}
			res = new Response(urlConn);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return res;
	}
	/**
	 * POST模拟用户访问
	 * @param url 网站入口
	 * @param param 参数
	 * @param enc 编码
	 * @return
	 */
	public Response postMethod(String url, Map<String,Object> param) {
		logger.debug("Reading POST: " + url);
		Response res = null;
		StringBuffer params = setParams(param);
		DataOutputStream out = null;
		try {
			URL u = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
			urlConn.setInstanceFollowRedirects(false);
			if(referer != null)
				urlConn.addRequestProperty("Referer", referer);
			setCookies(urlConn);
			urlConn.setRequestProperty("User-Agent", USER_AGENT_VALUE); 
			urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestMethod("POST");
			urlConn.connect();
			if(param != null){
				params.delete(0, 1);
				out = new DataOutputStream(urlConn.getOutputStream());
				out.writeBytes(params.toString());
				out.flush();
				out.close();
			}
			saveCookies(urlConn);
			String location = urlConn.getHeaderField("Location");
			if((urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) && location != null && redirect){
				this.referer = location;
				logger.debug("Redirect: " + location);
				return postMethod(location, null);
			}
			res = new Response(urlConn);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return res;
	}
	/**
	 * POST模拟用户访问
	 * @param url 网站入口
	 * @param param 参数(Object 类型可以传入file)
	 * @param enc 编码
	 * @return
	 */
	public Response postMultipartMethod(String url, Map<String,Object> param) {
		logger.debug("Reading POST: " + url);
		Response res = null;
		DataOutputStream out = null;
		try {
			URL u = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
			urlConn.setInstanceFollowRedirects(false);
			if(referer != null)
				urlConn.addRequestProperty("Referer", referer);
			setCookies(urlConn);
			urlConn.setRequestProperty("User-Agent", USER_AGENT_VALUE); 
			urlConn.setRequestProperty("Content-Type","multipart/form-data;boundary="+BOUNDARY);
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestMethod("POST");
			urlConn.connect();
			if(param != null){
				StringBuilder sb = new StringBuilder(); 
				out = new DataOutputStream(urlConn.getOutputStream());
				for (String key : param.keySet()) {
					Object obj = param.get(key);
					if(File.class.equals(obj.getClass())){
						File file = (File)obj;
						String filename = file.getName();
						String type = filename.substring(filename.lastIndexOf("."), filename.length());
						StringBuilder sb1=new StringBuilder();  
	                    sb1.append("--").append(BOUNDARY).append("\r\n").  
	                    append("Content-Disposition: form-data; name=\""+key+"\"; filename=\""+filename+"\"").  
	                    append("\r\n").append("Content-Type: "+type+"").append("\r\n\r\n");  
	                    out.write(sb1.toString().getBytes());  
	                      
	                    InputStream inStream = new FileInputStream(file);  
	                    if(inStream != null){  
	                        int len=-1;  
	                        byte[] buf=new byte[1024];  
	                        while((len=inStream.read(buf))!=-1){  
	                        	out.write(buf, 0, len);  
	                        	out.flush();  
	                        }  
	                        inStream.close();  
	                    }
	                    out.write("\r\n".getBytes());  
					}else{
						sb.append("--").append(BOUNDARY).append("\r\n").  
		                append("Content-Disposition: form-data; name=\""+key+"\"").  
		                append("\r\n\r\n").append(param.get(key)).append("\r\n");
					}
				}
				out.writeBytes(sb.toString());
				out.write(("--"+BOUNDARY+"--").getBytes());  
				out.flush();
				out.close();
			}
			saveCookies(urlConn);
			String location = urlConn.getHeaderField("Location");
			if((urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || urlConn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) && location != null && redirect){
				this.referer = location;
				logger.debug("Redirect: " + location);
				return postMethod(location, null);
			}
			res = new Response(urlConn);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return res;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	private String getHost(String url){
		int indexOf = url.indexOf("://");
		if(indexOf < 0)
			return url;
		String host = url.substring(indexOf + 3);
		int temp = host.indexOf("/");
		host = temp > -1 ? host.substring(0,temp) : host;
		temp = host.indexOf(":");
		host = temp > -1 ? host.substring(0,temp): host;
		return host;
	}
	
	private String getPath(URL url){
		String path = url.getPath();
		int indexOf = path.indexOf("/", path.indexOf("/") + 1);
		path = indexOf > -1 ? path.substring(0,indexOf) : "/";
		return path;
	}
	
	private String getPathByUrl(String url){
		String path = url.substring(url.indexOf("/",url.indexOf("://") + 3));
		path = path.substring(0, path.indexOf("/",path.indexOf("/")+1));
		return path;
	}
	
	private void setCookies(HttpURLConnection urlConn){
		URL url = urlConn.getURL();
		List<Cookie> list = cookies.get(url.getHost());
		if(list != null && list.size() > 0){
			String path = getPath(url);
			StringBuffer sb = new StringBuffer();
			for (Cookie cookie : list) {
				if(cookie.getPath().equals(path) || "/".equals(cookie.getPath())){
					sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
				}
			}
			logger.debug("Send Cookie:" + sb.toString());
			urlConn.setRequestProperty("Cookie", sb.toString());
		}
	}
	
	private String getPath(String cookie){
		String[] setCookies = cookie.split(";");
		for (String string : setCookies) {
			string = string.toLowerCase().trim();
			if(string.startsWith("path")){
				return string.split("=")[1];
			}
		}
		return "";
	}
	
	private void saveCookies(HttpURLConnection urlConn){
		Map<String, List<String>> header = urlConn.getHeaderFields();
		if(header.containsKey(COOKIE_KEY)){
			String host = urlConn.getURL().getHost();
			List<String> vlas = header.get(COOKIE_KEY);
			for (String string : vlas) {
				String path = getPath(string);
				Cookie cookie = new Cookie();
				cookie.setHost(host);
				cookie.setPath(path);
				String[] nameVal = string.split(";")[0].split("=");
				cookie.setName(nameVal[0].trim());
				cookie.setValue(nameVal[1].trim());
				receiveCookie(cookie, host);
			}
		}
	}
	private void receiveCookie(Cookie cookie, String host){
		addCookie(host, cookie);
		logger.debug("Receive Cookie:" + cookie);
	}

	public void addCookie(String url, Cookie cookie){
		String host = getHost(url);
		if(cookie.getHost() == null)
			cookie.setHost(host);
		if(cookie.getPath() == null)
			cookie.setPath(getPathByUrl(url));
		List<Cookie> list = cookies.get(host);
		if(list == null){
			list = new ArrayList<Cookie>();
			cookies.put(host, list);
		}
		for (Cookie cookie2 : list) {
			if(cookie2.getName().equals(cookie.getName()) && cookie2.getPath().equals(cookie.getPath())){
				cookie2.setCookie(cookie);
				return;
			}
		}
		list.add(cookie);
	}
	
	public String getUSER_AGENT_VALUE() {
		return USER_AGENT_VALUE;
	}

	public void setUSER_AGENT_VALUE(String uSER_AGENT_VALUE) {
		USER_AGENT_VALUE = uSER_AGENT_VALUE;
	}

	public boolean isFilterEmpty() {
		return isFilterEmpty;
	}

	public void setFilterEmpty(boolean isFilterEmpty) {
		this.isFilterEmpty = isFilterEmpty;
	}
}
