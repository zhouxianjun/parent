package com.gary.util;

import java.io.File;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class LocCode {
	public static String LOCLIST_PATH = null;
	
	public static JSONArray getLocs(){
		if(LOCLIST_PATH == null){
			LOCLIST_PATH = LocCode.class.getResource("/").getPath();
			LOCLIST_PATH += "LocList.json"; 
		}
		String json = FileUtils.readFile(new File(LOCLIST_PATH), "UTF-8");
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONArray locs = jsonObject.getJSONObject("Location").getJSONArray("CountryRegion");
		return locs;
	}
	
	public static void main(String[] args) throws IOException {
		JSONArray locs = getLocs();
		String addr = "中国广东省深圳市南山区";
		System.out.println(findAddr(addr, locs));
	}
	
	public static JSONArray autoNext(JSONObject json){
		return json.containsKey("State") ? json.getJSONArray("State") : json.containsKey("City") ? json.getJSONArray("City") : null;
	}
	
	public static JSONObject findAddr(String addr, JSONArray locs){
		for (Object arr : locs) {
			JSONObject c = JSONObject.fromObject(arr);
			String name = c.getString("Name");
			if(addr.startsWith(name)){
				String tmp = addr.replace(name + "省", "").replace(name + "市", "").replace(name, "");
				JSONArray next = autoNext(c);
				System.out.println(name + "--" + addr);
				if(next != null){
					return findAddr(tmp, next);
				}
				return c;
			}
		}
		return null;
	}
}
