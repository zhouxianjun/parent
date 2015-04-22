package com.gary.web.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.gary.web.util.I18nUtil;

/**
 * Servlet Filter implementation class LanguagesFilter
 */
public class LanguagesFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LanguagesFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String lang = req.getParameter("lang");
		Locale locale = getLocale(lang);
		if(locale != null){
			req.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	protected Locale getLocale(String lang){
		Locale locale = null;
		if(!StringUtils.isEmpty(lang)){
			if(lang.equalsIgnoreCase(I18nUtil.zh_CN))
				locale = Locale.CHINA;
			else if(lang.equalsIgnoreCase(I18nUtil.en_US))
				locale = Locale.US;
			else if(lang.equalsIgnoreCase(I18nUtil.zh_TW))
				locale = Locale.TAIWAN;
			else if(lang.equalsIgnoreCase(I18nUtil.ja_JP))
				locale = Locale.JAPAN;
		}
		return locale;
	}
}
