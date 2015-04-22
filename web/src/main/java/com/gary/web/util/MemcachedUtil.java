package com.gary.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemcachedUtil {
	private static MemcachedClient memcachedClient;
	
	private static final String SESSION_KEYS = "session_keys";
	
	private static Logger logger = LoggerFactory.getLogger(MemcachedUtil.class);
	public static <T> T get(String key){
		try {
			return memcachedClient.get(key);
		} catch (Exception e) {
			logger.error("从Memcached获取{}缓存数据错误!", key);
		}
		return null;
	}
	
	/**
	 * 删除缓存,不抛出异常
	 * @param key
	 * @return boolean
	 */
	public static boolean delete(String key){
		try {
			return memcachedClient.delete(key);
		} catch (Exception e) {
			logger.error("从Memcached删除{}缓存数据错误!", key);
		}
		return true;
	}
	public static void setMemcachedClient(MemcachedClient memcachedClient) {
		MemcachedUtil.memcachedClient = memcachedClient;
	}
	
	private static void updateSessionKeys(String key, HttpSession session){
		List<String> keys = get(SESSION_KEYS + session.getId());
		if(keys == null){
			keys = new ArrayList<String>();
		}
		if(!keys.contains(key)){
			keys.add(key);
		}
	}
	
	/**
	 * 设置缓存SESSION,失败后设置在HTTP
	 * @param key
	 * @param session
	 * @param val
	 */
	public static void setSession(String key, HttpSession session, Object val){
		try {
			memcachedClient.set(key + session.getId(), session.getMaxInactiveInterval(), val);
			updateSessionKeys(key, session);
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			logger.error("设置Memcached缓存{}SESSION{}缓存数据错误!", key, session.getId());
			session.setAttribute(key, val);
		}
	}
	
	/**
	 * 获取SESSION缓存,获取不到则取HTTP
	 * @param key
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSession(String key, HttpSession session){
		T t = get(key + session.getId());
		if(t == null && session != null){
			t = (T) session.getAttribute(key);
		}
		return t;
	}
	
	/**
	 * 清除SESSION缓存
	 * @param session
	 */
	public static void clearSession(HttpSession session){
		if(session != null){
			String sessionId = session.getId();
			List<String> keys = get(SESSION_KEYS + sessionId);
			if(keys != null){
				for (String string : keys) {
					delete(string + sessionId);
					session.removeAttribute(string);
				}
			}
			delete(SESSION_KEYS + sessionId);
			session.invalidate();
		}
	}
}
