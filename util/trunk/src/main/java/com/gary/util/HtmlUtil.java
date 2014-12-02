package com.gary.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gary.util.dto.DataMap;
import com.gary.util.dto.Listener;

public class HtmlUtil {
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
	/**
	 * 替换HTML标签属性内容
	 * @param content 内容
	 * @param tag 标签
	 * @param attr 属性
	 * @param listener map[attr]
	 * @return
	 */
	public static String replaceHtmlTag(String content, String tag, String attr, Listener listener) {
		String patternStr="<"+tag+"\\s*([^>]*)\\s*"+attr+"=\\\"(.*?)\\\"\\s*([^>]*)>"; 
		Pattern pattern = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(content);  
        String result = content;  
        DataMap<String, Object> data = new DataMap<String, Object>();
        while(matcher.find()) {  
            String src = matcher.group(2);  
            data.put(attr, src);
            result = result.replaceAll(src,listener.binding(data).toString());  
        }   
		return result;
	}
	/**
	 * 过滤掉标签
	 * @param str 内容
	 * @param tag 标签
	 * @return
	 */
	public static String filterHtmlTag(String str, String tag){
		String rex = "<"+tag+"\\s*([^>]*)>";
		str = matcher(str, rex);
		rex = "</"+tag+">";
		return matcher(str, rex);
	}
	
	private static String matcher(String str, String rex){
		Pattern pattern = Pattern.compile(rex);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	/**
	 * 过滤掉所有htm标签
	 * @param str 内容
	 * @return
	 */
	public static String filterHtml(String str) {
		return matcher(str, regxpForHtml);
	}
	/**
	 * 判断标记是否存在
	 * @param input
	 * @return
	 */
	public boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i <= input.length() - 1; i++) {
				c = input.charAt(i);
				switch (c) {
					case '>':
						flag = true;
						break;
					case '<':
						flag = true;
						break;
					case '"':
						flag = true;
						break;
					case '&':
						flag = true;
						break;
				}
			}
		}
		return flag;
	}
	/**
	 * 替换标记
	 * @param input
	 * @return
	 */
	public String replaceTag(String input) {
		if (!hasSpecialChars(input)) {
			return input;
		}
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i <= input.length() - 1; i++) {
			c = input.charAt(i);
			switch (c) {
				case '<':
					filtered.append("&lt;");
					break;
				case '>':
					filtered.append("&gt;");
					break;
				case '"':
					filtered.append("&quot;");
					break;
				case '&':
					filtered.append("&amp;");
					break;
				default:
					filtered.append(c);
			}
		}
		return (filtered.toString());
	}
	public static void main(String[] args) {
		String str = "sdsdsdsd<a></a><div></div><img src=${imgServer!}${(content.subcontent.contentImg)!}/>";
		System.out.println(filterHtmlTag(str,"a"));
		System.out.println(filterHtml(str));
	}
}
