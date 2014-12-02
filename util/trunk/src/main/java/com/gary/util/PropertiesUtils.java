package com.gary.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
	/**
	 * 读取Properties配置文件
	 * 
	 * @param path
	 *            文件路径 如果在项目根目录下 就直接写名字
	 * @return Properties 例子：p.getProperty("ip")
	 * @throws IOException
	 */
	public static Properties getProperties(String path) throws IOException {
		InputStream inputStream = new BufferedInputStream(new FileInputStream(
				path));
		Properties p = new Properties();
		p.load(inputStream);
		return p;
	}
	
	public static Map<Object, Object> toMap(Properties p){
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (Map.Entry<Object, Object> entry : p.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
}
