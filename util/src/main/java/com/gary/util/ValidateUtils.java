package com.gary.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类主要是用来校验用户信息
 * @author Gary
 *
 */
public class ValidateUtils {
	/**sip:accessnum@ip[:port]*/
	public static final String SIPACCESSNUMBER = "sip:\\d+@((((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))|(\\w*))(:\\d+)?";
	/**邮箱*/
	public static final String EMAILVALIDATE = "(?:\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3}$)";
	/**正浮点数*/
	public static final String NUMBERVALIDATE = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
	/**4到15位字母数字下划线*/
	public static final String USERNAMEVALIDATE = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
	/**6到15位字母数字下划线*/
	public static final String PASSWORDVALIDATE = "^[a-zA-Z][a-zA-Z0-9_]{5,15}$";
	/**正整数*/
	public static final String ZHENGNUMBER = "^\\d+$";
	/**邮编*/
	public static final String ZIP = "^[1-9]\\d{5}$";
	/**国内11位手机号码*/
	public static final String MOBILE = "^1\\d{10}$";
	/**8位号码*/
	public static final String TTNUMBER = "^\\d{8}$";
	/**国内座机号码*/
	public static final String PHONE = "^(\\d{3}-|\\d{4}-){1}(\\d{8}|\\d{7}){1}$";
	/**不得为标点符号*/
	public static final String ADDRESS = "^\\P{Punct}+$";
	/**年4位或2位，月2位*/
	public static final String DATE = "^(\\d{2}|\\d{4})-((0([1-9]{1}))|(1[1|2]))";
	/**不可以有标点符号和数字*/
	public static final String TRUENAME = "^([\\P{Punct}&&[^0-9]])+$";
	/**字母或数字*/
	public static final String CERTIFICATE = "^([A-Za-z]||\\d)+$";
	/**数字*/
	public static final String NUMBER = "^\\d+$";
	/**IP*/
	public static final String IP = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
	/**url*/
	public static final String URL = "[a-zA-z]+://[^\\s]*";
	/**html*/
	public static final String HTML = "<(\\S*?)[^>]*>.*?|<.*? />";
	/**日期*/
	public static final String DATE_YYYY_MM_DD = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
	/**时间 00:00:00~23:59:59*/
	public static final String TIME = "(([0-1][0-9])|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	/** 判断 注释*/
	public static final String ANNOTATE = "/\\*.*\\*/";
	/**
	 * 判断接受的长度
	 * @param input 要检查的字符串
	 * @param i 长度
	 * @return boolean
	 */
	public boolean ValidateLeng(String input, int i) {
		if (input.length() > i) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断IP地址是否合法
	 * @param ip 要检查的IP地址
	 * @return boolean
	 */
	public static boolean iPValidate(String ip) {
		Pattern pattern = Pattern.compile(IP);
		Matcher mc = pattern.matcher(ip);
		return mc.matches();
	}
	/**
	 * 判断固定电话
	 * @param phone 要检查的固定电话
	 * @return boolean
	 */
	public static boolean phoneValidate(String phone){//
		Pattern pa = Pattern.compile(PHONE);
		Matcher mc = pa.matcher(phone);
		return mc.matches();
	}
	/**
	 * 判断 email的格式
	 * @param email
	 * @return boolean
	 */
	public static boolean emailValidate(String email) {// �ж�
		Pattern pattern = Pattern.compile(EMAILVALIDATE);
		Matcher mc = pattern.matcher(email);
		return mc.matches();
	}
	/**
	 * 判断 number的格式(正浮点数)
	 * @param number
	 * @return boolean
	 */
	public static boolean numberValidate(String number) {// �ж�
		Pattern pattern = Pattern.compile(NUMBERVALIDATE);
		Matcher mc = pattern.matcher(number);
		return (mc.matches());
	}
	/**
	 * 判断name的格式必须字母开始长度为5到15，由字母和数字组成
	 * @param name
	 * @return boolean
	 */
	public static boolean nameValidate(String name) {// 
		Pattern pattern = Pattern.compile(USERNAMEVALIDATE);
		Matcher mc = pattern.matcher(name);
		return mc.matches();
	}
	/**
	 * 判断password的格式必须字母开始长度为6到15，由字母和数字组成������ĸ���������
	 * @param password
	 * @return boolean
	 */
	public static boolean passwordValidate(String password) {
		Pattern pattern = Pattern.compile(PASSWORDVALIDATE);
		Matcher mc = pattern.matcher(password);
		return mc.matches();
	}
	/**
	 * 判断 zip的格式
	 * @param zip
	 * @return boolean
	 */
	public static boolean zipValidate(String zip) {// �ĸ�ʽ
		Pattern pattern = Pattern.compile(ZIP);
		Matcher mc = pattern.matcher(zip);
		return mc.matches();
	}
	/**
	 * 判断 国内11位手机号码的格式
	 * @param phone
	 * @return boolean
	 */
	public static boolean mobileValidate(String phone)
	{
		Pattern pattern1 = Pattern.compile(MOBILE);
		Matcher mc1 = pattern1.matcher(phone);
		return (mc1.matches());
	}
	/**
	 * 判断 date的格式(年4位或2位，月2位)
	 * @param date
	 * @return boolean
	 */
	public static boolean dateValidate(String date) {
		Pattern pattern = Pattern.compile(DATE);
		Matcher mc = pattern.matcher(date);
		return mc.matches();
	}
	/**
	 * 判断 正整数的格式
	 * @param number
	 * @return boolean
	 */
	public static boolean zhengshuValidate(String number) {
		Pattern pattern = Pattern.compile(ZHENGNUMBER);
		Matcher mc = pattern.matcher(number);
		return mc.matches();
	}
	/**
	 * 判断 URL的格式
	 * @param url
	 * @return boolean
	 */
	public static boolean urlValidate(String url) {
		Pattern pattern = Pattern.compile(URL);
		Matcher mc = pattern.matcher(url);
		return mc.matches();
	}
	/**
	 * 判断 日期的格式(2011-01-01)
	 * @param date
	 * @return boolean
	 */
	public static boolean dateValidate1(String date) {
		Pattern pattern = Pattern.compile(DATE_YYYY_MM_DD);
		Matcher mc = pattern.matcher(date);
		return mc.matches();
	}
	/**
	 * 判断 时间的格式
	 * @param time
	 * @return boolean
	 */
	public static boolean timeValidate(String time) {
		Pattern pattern = Pattern.compile(TIME);
		Matcher mc = pattern.matcher(time);
		return mc.matches();
	}
	/**
	 * 判断 HTML的格式
	 * @param html
	 * @return boolean
	 */
	public static boolean htmlValidate(String html) {
		Pattern pattern = Pattern.compile(HTML);
		Matcher mc = pattern.matcher(html);
		return mc.matches();
	}
	/**
	 * 不得为标点符号
	 * @param address
	 * @return boolean
	 */
	public static boolean addressValidate(String address) {// 判断 date的格式
		Pattern pattern = Pattern.compile(ADDRESS);
		Matcher mc = pattern.matcher(address);
		return mc.matches();
	}
	/**
	 * 判断 rate的格式必须小于1
	 * @param rate
	 * @return boolean
	 */
	public static boolean rateValidate(String rate) {// 判断 rate的格式必须小于1
		Pattern pattern = Pattern.compile(NUMBERVALIDATE);
		Matcher mc = pattern.matcher(rate);
		if (mc.matches()) {
			long i = Long.parseLong(rate);
			if (i > 1 || i < 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	/**
	 * 真实姓名必须完全由字母组成
	 * @param truename
	 * @return boolean
	 */
	public static boolean trueNameValidate(String truename) {//
		Pattern pattern = Pattern.compile(TRUENAME);
		Matcher mc = pattern.matcher(truename);
		return mc.matches();
	}
	/**
	 * 字母或数字
	 * @param Certificate
	 * @return boolean
	 */
	public static boolean Certificate(String Certificate) {
		Pattern pattern = Pattern.compile(CERTIFICATE);
		Matcher mc = pattern.matcher(Certificate);
		return mc.matches();
	}
	/**
	 * sip:accessnum@ip[:port]验证
	 * @param accessSip
	 * @return boolean
	 */
	public static boolean sipAccessValidate(String accessSip) {
		Pattern pattern = Pattern.compile(SIPACCESSNUMBER);
		Matcher mc = pattern.matcher(accessSip);
		return mc.matches();
	}
	/**
	 * 18位身份证判断
	 * @param arrIdCard
	 * @return boolean
	 */
	public static boolean isIdCard(char[] arrIdCard) {
		// 判断长度
		if (arrIdCard == null || arrIdCard.length != 18)
			return false;

		// 判断校验码
		int sigma = 0;
		int a[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char w[] = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		for (int i = 0; i < 17; i++) {
			int ai = arrIdCard[i] - '0';
			int wi = a[i];
			sigma += ai * wi;
		}

		int number = sigma % 11;
		if (arrIdCard[17] != w[number]) {
			return false;
		}

		return true;
	}
	/**
	 * 判断数字
	 * @param number
	 * @return boolean
	 */
	public static boolean Number(String number){//
		Pattern pa = Pattern.compile(NUMBER);
		Matcher mc = pa.matcher(number);
		return mc.matches();
	}
	/**
	 * 判断注释符/*
	 * @param number
	 * @return boolean
	 */
	public static boolean annotate(String annotate){//
		Pattern pa = Pattern.compile(ANNOTATE);
		Matcher mc = pa.matcher(annotate);
		return mc.matches();
	}
	/** 
     * 删除input字符串中的html格式 
     * 再判断字符串的长度(-1则不判断,如小于length则substring(0, length)再加上"ERROR") 
     * @param input 
     * @param length 
     * @return String
     */  
    public static String splitAndFilterString(String input, int length) {  
        if (input == null || input.trim().equals("")) {  
            return "";  
        }  
        // 去掉所有html元素,  
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(  
                "<[^>]*>", "");  
        str = str.replaceAll("[(/>)<]", "");  
        int len = str.length();
        if(length!=-1){
	        if (len <= length) {  
	            return str;  
	        } else {  
	            str = str.substring(0, length);  
	            str += "ERROR";  
	        }  
        }
        return str;  
    }  
    /**
	 * 判断length位数字
	 * @param number
	 * @return  boolean
	 */
	public static boolean lengthNumber(String number,int length){//
		String TTNUMBER = "^\\d{"+length+"}$";
		Pattern pa = Pattern.compile(TTNUMBER);
		Matcher mc = pa.matcher(number);
		return mc.matches();
	}
}
