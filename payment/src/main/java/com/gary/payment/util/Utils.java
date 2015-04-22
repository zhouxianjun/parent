package com.gary.payment.util;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		/**
	    * 金额补零2位小数
	    * @param decimal
	    * @param count
	    * @return String
	    */
	   public static String parseMoney(BigDecimal decimal, int count) {
			try {
				String money = String.format("%.2f", decimal).replace(".", "");
				int bw = count - money.length();
				if(bw > 0){
					money = String.format("%0"+ bw +"d", 0) + money;
				}
				return money;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	   
	   /**
	    * 金额补零的反转
	    * @param money
	    * @param count
	    * @return BigDecimal
	    */
		public static BigDecimal parseMoney(String money, int count) {
			try {
				if(money.length() == count){
					for (int i = 0; i < money.length(); i++) {
						int ci = Integer.valueOf(String.valueOf(money.charAt(i)));
						if(ci > 0){
							money = money.substring(i);
							BigDecimal m = new BigDecimal(money).divide(BigDecimal.valueOf(100));
							return m;
						}
					}
				}
			} catch (Exception e) {}
			return null;
		}
		
		public static boolean isNumeric(CharSequence cs) {
	        if (cs == null || cs.length() == 0) {
	            return false;
	        }
	        int sz = cs.length();
	        for (int i = 0; i < sz; i++) {
	            if (Character.isDigit(cs.charAt(i)) == false) {
	                return false;
	            }
	        }
	        return true;
	    }
		
		public static String toYYYYMMDD(Date date){
			return sdf.format(date);
		}
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

}
