package com.gary.util.dto;

import java.util.HashMap;

public class DataMap<K, V> extends HashMap<K, V>{
	private static final long serialVersionUID = -6633468987808590167L;

	@SuppressWarnings("unchecked")
	public <T> T get(K key, Class<T> c){
		return (T)get(key);
	}
}
