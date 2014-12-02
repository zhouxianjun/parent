package com.gary.util;

import java.math.BigDecimal;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

/**
 * 算法工具类
 * 
 * @author Gary
 * 
 */
public class AlgorithmUtils {
	private static Logger logger = Logger.getLogger(AlgorithmUtils.class);
	/**
	 * MD5加密
	 * 
	 * @param s
	 *            要加密的字符串
	 * @return 加密后的字符串
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字符串转16进制
	 * 
	 * @param s
	 * @return
	 */
	public static String toStringHex(String s) {
		byte[] byStr = new byte[s.length() / 2];
		for (int i = 0; i < byStr.length; i++) {
			try {
				byStr[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		try {
			s = new String(byStr, "utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * 16进制转字符串
	 * @param str
	 * @return
	 */
	public static String toHexString(String str) {
		byte[] byStr = str.getBytes();
		return parseArr(byStr);
	}

	private static String parseArr(byte[] byStr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byStr.length; i++) {
			sb.append("%" + Integer.toHexString(byStr[i]));
		}
		return sb.toString();
	}

	/**
	 * 精确计算减法 balance-money 保留v位小数
	 * 
	 * @param balance
	 * @param money
	 * @param v
	 * @return float
	 */
	public static float subtractBlance(float balance, float money, int v) {
		if (balance != 0 && balance >= money) {
			return new BigDecimal(balance).subtract(new BigDecimal(money))
					.setScale(v, BigDecimal.ROUND_HALF_UP).floatValue();
		}
		return 0;
	}

	/**
	 * 精确计算加法 balance+money 保留v位小数
	 * 
	 * @param balance
	 * @param money
	 * @param v
	 * @return float
	 */
	public static float addBlance(float balance, float money, int v) {
		if (balance != 0 && balance >= money) {
			return new BigDecimal(balance).add(new BigDecimal(money))
					.setScale(v, BigDecimal.ROUND_HALF_UP).floatValue();
		}
		return 0;
	}

	/**
	 * 精确计算乘法 balance*money 保留v位小数
	 * 
	 * @param balance
	 * @param money
	 * @param v
	 * @return float
	 */
	public static float multiplyBlance(float balance, float money, int v) {
		if (balance != 0 && balance >= money) {
			return new BigDecimal(balance).multiply(new BigDecimal(money))
					.setScale(v, BigDecimal.ROUND_HALF_UP).floatValue();
		}
		return 0;
	}

	/**
	 * 精确计算除法 balance/money 保留v位小数
	 * 
	 * @param balance
	 * @param money
	 * @param v
	 * @return float
	 */
	public static float divideBlance(float balance, float money, int v) {
		if (balance != 0 && balance >= money) {
			return new BigDecimal(balance).divide(new BigDecimal(money))
					.setScale(v, BigDecimal.ROUND_HALF_UP).floatValue();
		}
		return 0;
	}
}
