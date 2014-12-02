package com.gary.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.gary.util.code.Coder;
import com.gary.util.dto.IPDto;


public class Utils {
	private static Logger logger = Logger.getLogger(Utils.class);
	/**
	 * 读取email url
	 * @deprecated 内部使用
	 * @param url
	 * @param enc
	 * @return 返回远程数据String
	 */
	public static String readWebByMail(String url, String enc, int time) {
		boolean readUrl = true;
		BufferedReader br = null;
		InputStream in = null;
		StringBuilder sb = new StringBuilder();
		try {
			String str = null;
			URL u = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
			urlConn.setConnectTimeout(time);
			urlConn.setReadTimeout(time);
			if(urlConn.getContentType() == null || urlConn.getContentType().indexOf("text/html") < 0){
				readUrl = false;
				urlConn = null;
				sb = null;
				return null;
			}
			in = urlConn.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, enc));
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			str = null;
			urlConn = null;
		} catch (IOException e) {
			readUrl = false;
			return null;
		} finally {
			try {
				if (readUrl) {
					br.close();
					in.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return sb.toString();
	}
	private static int getRandomM(int num) {
		if (num > 10) {
			return 0;
		}
		int x = 1;
		for (int i = 1; i < num; i++) {
			x = x * 10;
		}
		return x;
	}

	private static int getRandomN(int num) {
		String x = "1";
		int n = 0;
		for (int i = 1; i < num; i++) {
			x = x + "1";
		}
		if (x.length() < 11) {
			long ints = Long.valueOf("2147483647").longValue();
			n = Long.valueOf(x).longValue() > ints ? n : Integer.valueOf(x);
		}
		return n;
	}

	/**
	 * 获得随机数
	 * 
	 * @param num
	 *            int<11
	 * @return String
	 */
	public static String getRandomCode(int num) {
		int n = 9 * getRandomN(num);
		int m = getRandomM(num);
		int intCount = (new Random()).nextInt(n);
		if (intCount < m)
			intCount += m;
		String code = intCount + "";
		return code;
	}

	/**
	 * 反射调用方法
	 * 
	 * @author Gary
	 * @param object
	 *            要反射的实体类
	 * @param methodName
	 *            反射类的方法名
	 * @param parameterVlaues
	 *            反射类的方法的参数 没有可以填NULL
	 * @return 一个Object[]对象 [0]=error [1]=方法的返回 [2]=Utils.class实体类[3]=error 异常类
	 */
	public static Object[] executionMethodByEntity(Object obj,
			String methodName, Object[] parameterVlaues) {
		Object[] re = new Object[4];
		if (obj == null || methodName == null
				|| "".equals(methodName.trim())) {
			re[0] = "参数不足!";
			return re;
		}
		/** 存放该方法的解释 */
		String thisMethodParameter = "";
		/** 存放传入方法的解释 */
		String youMethodParameter = "";
		/** 存放error */
		String error = "";
		try {
			/** 根据对象反射一个Class */
			Class<?> c = Class.forName(obj.getClass().getName());
			thisMethodParameter = c.getName() + "(";
			youMethodParameter = c.getName() + "(";
			/** 得到该类的所有方法 */
			Method[] methods = c.getMethods();
			/** 得到该类的指定methodName的方法的参数类型 */
			Class<?> pclass[] = null;
			List<String> methodParameterType = new ArrayList<String>();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					pclass = method.getParameterTypes();
					for (Class<?> class1 : pclass) {
						thisMethodParameter = thisMethodParameter
								+ class1.getName() + ",";
						methodParameterType.add(class1.getSimpleName());
					}
					if(pclass != null && pclass.length > 0)
						thisMethodParameter = thisMethodParameter.substring(0,
								thisMethodParameter.length() - 1)
								+ ")";
					else
						thisMethodParameter = thisMethodParameter + ")";
				}
			}
			/** 如果给出的参数个数与得到的参数不同则返回 */
			if (parameterVlaues == null && methodParameterType.size() > 0) {
				re[0] = ">>>>>参数个数错误！argument type mismatch<<<<<\n此方法需要"
						+ methodParameterType.size() + "个参数:"
						+ thisMethodParameter;
				return re;
			}
			Method m = c.getMethod(methodName, pclass);
			try {
				re[1] = m.invoke(c.newInstance(), parameterVlaues);
				re[2] = c.newInstance();
				return re;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
		} catch (SecurityException e) {
			error = "不信任的不安全的调用:" + e.getMessage();
			re[3] = e;
		} catch (IllegalArgumentException e) {
			if (parameterVlaues != null)
				for (Object object2 : parameterVlaues) {
					youMethodParameter = youMethodParameter + object2.getClass().getName()
							+ ",";
				}
			if(parameterVlaues != null && parameterVlaues.length > 0)
				youMethodParameter = youMethodParameter.substring(0,
						youMethodParameter.length() - 1)
						+ ")";
			else
				youMethodParameter = youMethodParameter + ")";
			error = ">>>>>参数类型错误！" + e.getMessage() + "<<<<<\n该方法需要的类型:"
					+ thisMethodParameter + "\n您传的参数类型:" + youMethodParameter;
			re[3] = e;
		} catch (NoSuchMethodException e) {
			error = "该类没有此方法:" + e.getMessage();
			re[3] = e;
		} catch (ClassNotFoundException e) {
			error = "找不到该类:" + e.getMessage();
			re[3] = e;
		} catch (IllegalAccessException e) {
			error = "空错误:" + e.getMessage();
			re[3] = e;
		} catch (InvocationTargetException e) {
			error = "调用方法内部未知错误:" + e.getMessage() + "请检查调用的该方法";
			re[3] = e;
		}
		re[0] = error;
		return re;
	}

	/**
	 * 反射调用方法
	 * 
	 * @author Gary
	 * @param object
	 *            要反射的实体类的全名例如: com.gary.Utils
	 * @param methodName
	 *            反射类的方法名
	 * @param parameterVlaues
	 *            反射类的方法的参数 没有可以填NULL
	 * @return 一个Object[]对象 [0]=error [1]=方法的返回 [2]=Utils.class实体类[3]=error 异常类
	 */
	public static Object[] executionMethodByEntity(String obj,
			String methodName, Object[] parameterVlaues) {
		Object[] re = new Object[4];
		if (obj == null || methodName == null
				|| "".equals(methodName.trim()) || "".equals(obj.trim())) {
			re[0] = "参数不足!";
			return re;
		}
		/** 存放该方法的解释 */
		String thisMethodParameter = "";
		/** 存放传入方法的解释 */
		String youMethodParameter = "";
		/** 存放error */
		String error = "";
		try {
			/** 根据对象反射一个Class */
			Class<?> c = Class.forName(obj);
			thisMethodParameter = c.getName() + "(";
			youMethodParameter = c.getName() + "(";
			/** 得到该类的所有方法 */
			Method[] methods = c.getMethods();
			/** 得到该类的指定methodName的方法的参数类型 */
			Class<?> pclass[] = null;
			List<String> methodParameterType = new ArrayList<String>();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					pclass = method.getParameterTypes();
					for (Class<?> class1 : pclass) {
						thisMethodParameter = thisMethodParameter
								+ class1.getName() + ",";
						methodParameterType.add(class1.getSimpleName());
					}
					if(pclass != null && pclass.length > 0)
						thisMethodParameter = thisMethodParameter.substring(0,
								thisMethodParameter.length() - 1)
								+ ")";
					else
						thisMethodParameter = thisMethodParameter + ")";
				}
			}
			/** 如果给出的参数个数与得到的参数不同则返回 */
			if (parameterVlaues == null && methodParameterType.size() > 0) {
				re[0] = ">>>>>参数个数错误！argument type mismatch<<<<<\n此方法需要"
						+ methodParameterType.size() + "个参数:"
						+ thisMethodParameter;
				return re;
			}
			Method m = c.getMethod(methodName, pclass);
			try {
				re[1] = m.invoke(c.newInstance(), parameterVlaues);
				re[2] = c.newInstance();
				return re;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
		} catch (SecurityException e) {
			error = "不信任的不安全的调用:" + e.getMessage();
			re[3] = e;
		} catch (IllegalArgumentException e) {
			if (parameterVlaues != null)
				for (Object object2 : parameterVlaues) {
					youMethodParameter = youMethodParameter + object2.getClass().getName()
							+ ",";
				}
			if(parameterVlaues != null && parameterVlaues.length > 0)
				youMethodParameter = youMethodParameter.substring(0,
						youMethodParameter.length() - 1)
						+ ")";
			else
				youMethodParameter = youMethodParameter + ")";
			error = ">>>>>参数类型错误！" + e.getMessage() + "<<<<<\n该方法需要的类型:"
					+ thisMethodParameter + "\n您传的参数类型:" + youMethodParameter;
			re[3] = e;
		} catch (NoSuchMethodException e) {
			re[3] = e;
			error = "该类没有此方法:" + e.getMessage();
		} catch (ClassNotFoundException e) {
			error = "找不到该类:" + e.getMessage();
			re[3] = e;
		} catch (IllegalAccessException e) {
			error = "空错误:" + e.getMessage();
			re[3] = e;
		} catch (InvocationTargetException e) {
			error = "调用方法内部未知错误:" + e.getMessage() + "请检查调用的该方法";
			re[3] = e;
		}
		re[0] = error;
		return re;
	}
	
	/**
	 * 反射调用方法
	 * 
	 * @author Gary
	 * @param object
	 *            要反射的实体类的路径 例如：Utils.class
	 * @param methodName
	 *            反射类的方法名
	 * @param parameterVlaues
	 *            反射类的方法的参数 没有可以填NULL
	 * @return 一个Object[]对象 [0]=error [1]=方法的返回 [2]=Utils.class实体类 [3]=error 异常类
	 */
	public static Object[] executionMethodByEntity(Class<?> obj,
			String methodName, Object[] parameterVlaues) {
		Object[] re = new Object[4];
		if (obj == null || methodName == null
				|| "".equals(methodName.trim())) {
			re[0] = "参数不足!";
			return re;
		}
		/** 存放该方法的解释 */
		String thisMethodParameter = "";
		/** 存放传入方法的解释 */
		String youMethodParameter = "";
		/** 存放error */
		String error = "";
		try {
			/** 根据对象反射一个Class */
			Class<?> c = Class.forName(obj.getName());
			thisMethodParameter = c.getName() + "(";
			youMethodParameter = c.getName() + "(";
			/** 得到该类的所有方法 */
			Method[] methods = c.getMethods();
			/** 得到该类的指定methodName的方法的参数类型 */
			Class<?> pclass[] = null;
			List<String> methodParameterType = new ArrayList<String>();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					pclass = method.getParameterTypes();
					for (Class<?> class1 : pclass) {
						thisMethodParameter = thisMethodParameter
								+ class1.getName() + ",";
						methodParameterType.add(class1.getSimpleName());
					}
					if(pclass != null && pclass.length > 0)
						thisMethodParameter = thisMethodParameter.substring(0,
								thisMethodParameter.length() - 1)
								+ ")";
					else
						thisMethodParameter = thisMethodParameter + ")";
				}
			}
			/** 如果给出的参数个数与得到的参数不同则返回 */
			if (parameterVlaues == null && methodParameterType.size() > 0) {
				re[0] = ">>>>>参数个数错误！argument type mismatch<<<<<\n此方法需要"
						+ methodParameterType.size() + "个参数:"
						+ thisMethodParameter;
				return re;
			}
			Method m = c.getMethod(methodName, pclass);
			try {
				re[1] = m.invoke(c.newInstance(), parameterVlaues);
				re[2] = c.newInstance();
				return re;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
			}
		} catch (SecurityException e) {
			error = "不信任的不安全的调用:" + e.getMessage();
			re[3] = e;
		} catch (IllegalArgumentException e) {
			if (parameterVlaues != null)
				for (Object object2 : parameterVlaues) {
					youMethodParameter = youMethodParameter + object2.getClass().getName()
							+ ",";
				}
			if(parameterVlaues != null && parameterVlaues.length > 0)
				youMethodParameter = youMethodParameter.substring(0,
						youMethodParameter.length() - 1)
						+ ")";
			else
				youMethodParameter = youMethodParameter + ")";
			error = ">>>>>参数类型错误！" + e.getMessage() + "<<<<<\n该方法需要的类型:"
					+ thisMethodParameter + "\n您传的参数类型:" + youMethodParameter;
			re[3] = e;
		} catch (NoSuchMethodException e) {
			error = "该类没有此方法:" + e.getMessage();
			re[3] = e;
		} catch (ClassNotFoundException e) {
			error = "找不到该类:" + e.getMessage();
			re[3] = e;
		} catch (IllegalAccessException e) {
			error = "空错误:" + e.getMessage();
			re[3] = e;
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
			error = "调用方法内部未知错误:" + e.getMessage() + "请检查调用的该方法";
			re[3] = e;
		}
		re[0] = error;
		return re;
	}

	/**
	 * 得到类型Class
	 * 
	 * @param type
	 *            类型
	 * @param types
	 *            扩展类型
	 * @return Class
	 */
	public static Class<?> getParameterType(String type, List<Class<?>> types) {
		if ("String".equalsIgnoreCase(type)) {
			return String.class;
		} else if ("int".equalsIgnoreCase(type)
				|| "Integer".equalsIgnoreCase(type)) {
			return int.class;
		} else if ("long".equalsIgnoreCase(type)) {
			return long.class;
		} else if ("double".equalsIgnoreCase(type)) {
			return double.class;
		} else if ("float".equalsIgnoreCase(type)) {
			return float.class;
		} else if ("short".equalsIgnoreCase(type)) {
			return short.class;
		} else if ("byte".equalsIgnoreCase(type)) {
			return byte.class;
		} else if ("char".equalsIgnoreCase(type)
				|| "Character".equalsIgnoreCase(type)) {
			return char.class;
		} else if ("boolean".equalsIgnoreCase(type)) {
			return boolean.class;
		} else if ("String[]".equalsIgnoreCase(type)) {
			return String[].class;
		}else {
			if (types != null) {
				for (Class<?> cn : types) {
					if (cn.getSimpleName().equalsIgnoreCase(type)) {
						return cn;
					}
				}
			}
			return Object.class;
		}
	}

	/**
	 * 接口传送数据
	 * @deprecated 请使用HttpClient
	 * @param path
	 *            接口地址URL例如:http://be.ccwe.cn/ProcessRec/UserRegRec.ashx
	 * @param params
	 *            要传送的参数 例如:name=1003&pwd=111111
	 * @return String[] [0]=数据 [1]="true" or "false"
	 */
	public static String[] Interfaces(String path, String params) {
		BufferedReader in = null;// 读取响应输入流
		PrintWriter out = null;
		String[] result = new String[2];// 返回的结果
		try {
			// 创建URL对象
			URL connURL = new URL(path);
			// 打开URL连接
			HttpURLConnection httpConn = (HttpURLConnection) connURL
					.openConnection();
			// 设置通用属性
			httpConn.setConnectTimeout(10000);
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
			httpConn.setRequestProperty("Accept-Charset", "GBK");
			httpConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=gbk");
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			out.write(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn
					.getInputStream(), "utf-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result[0] += line + "\r\n";
			}
			result[1] = "true";
		} catch (IllegalArgumentException e) {
			System.out.println(">>>>>>>>>>>>>>>>>连接超时...");
			result[1] = "false";
		} catch (Exception e) {
			System.out.println(">>>>>>>>>>>>>>>>>未知错误...");
			result[1] = "false";
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				result[1] = "false";
			}
		}
		return result;
	}

	/**
	 * 反射一个对象的包含methodPrefix的方法名
	 * 
	 * @param object
	 *            对象
	 * @param methodPrefix
	 *            方法名前缀
	 * @return List<Method>集合
	 * @throws ClassNotFoundException
	 */
	public static List<Method> getObjectMethod(Object object,
			String methodPrefix) throws ClassNotFoundException {
		if (object == null || methodPrefix == null || "".equals(methodPrefix)) {
			return null;
		}

		/** 根据对象反射一个Class */
		Class<?> c = Class.forName(object.getClass().getName());
		/** 得到该类的所有方法 */
		Method[] methods = c.getDeclaredMethods();
		/** 得到该类的指定methodName的方法的参数类型 */
		List<Method> methodByGet = new ArrayList<Method>();
		for (final Method method : methods) {
			if (method.getName().indexOf(methodPrefix) > -1) {
				methodByGet.add(method);
			}
		}
		return methodByGet;
	}

	/**
	 * 格式化类名
	 * 
	 * @param str
	 *            必须是类名 英文名 不能是中文
	 * @return String
	 */
	public static String toUpCase(String str) {
		String index = str.substring(0, 1);
		str = index.toUpperCase()
				+ str.substring(1, str.length()).toLowerCase();
		return str;
	}

	/**
	 * 获取本机外网IP
	 * @return
	 */
	public static IPDto getExNetIp(){
		IPDto ip = new IPDto();
		HttpClient http = Utils.getInstance(HttpClient.class);
		String ipu = http.getMethod("http://checkip.dyndns.org", null).get(String.class);
		Pattern pattern=Pattern.compile("(\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})[.](\\d{1,3})", Pattern.CASE_INSENSITIVE);    
        Matcher matcher=pattern.matcher(ipu);        
        while(matcher.find()){
        	ipu = matcher.group(0);
        }  
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("format", "json");
        p.put("ip", ipu);
        String ipdesc = http.getMethod("http://int.dpool.sina.com.cn/iplookup/iplookup.php", p).get(String.class);
		ipdesc = Coder.decodeUnicode(ipdesc);
		JSONObject json = JSONObject.fromObject(ipdesc);
		ip.setIp(ipu);
		ip.setCountry(json.getString("country"));
		ip.setProvince(json.getString("province"));
		ip.setCity(json.getString("city"));
		ip.setIsp(json.getString("isp"));
		ipu = null;
		http = null;
		p = null;
		ipdesc = null;
		json = null;
		return ip;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getInstance(Class<T> c){
		T obj = StaticMemory.data.get(c.getName(),c);
		if(obj != null)
			return obj;
		try {
			Constructor constr = Class.forName(c.getName()).getDeclaredConstructor();
			constr.setAccessible(true);
			obj = (T)constr.newInstance();
			StaticMemory.data.put(c.getName(), obj);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return obj;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getInstance(Class<T> c, Object... params){
		T obj = StaticMemory.data.get(c.getName(),c);
		if(obj != null)
			return obj;
		try {
			Constructor constr = Class.forName(c.getName()).getDeclaredConstructor();
			constr.setAccessible(true);
			obj = (T)constr.newInstance(params);
			StaticMemory.data.put(c.getName(), obj);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return obj;
	}
	public static final InputStream byte2Input(byte[] buf) {  
        return new ByteArrayInputStream(buf);  
    }  
  
    public static final byte[] input2byte(InputStream inStream)  
            throws IOException {  
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
        byte[] buff = new byte[100];  
        int rc = 0;  
        while ((rc = inStream.read(buff, 0, 100)) > 0) {  
            swapStream.write(buff, 0, rc);  
        }  
        byte[] in2b = swapStream.toByteArray();  
        return in2b;  
    }
    
    public static int string2Int(String str){
    	Pattern p = Pattern.compile("[^0-9]");   
    	Matcher m = p.matcher(str);   
    	String trim = m.replaceAll("").trim();
    	if("".equals(trim))
    		return 0;
    	return Integer.valueOf(trim);
    }
	public static void main(String[] args) throws Exception {
//		new Thread(new Sdsd()).start();
		PropertiesUtils.toMap(PropertiesUtils.getProperties("E:\\Gary\\j2ee\\gary-core\\src\\log4j.properties"));
	}
}
