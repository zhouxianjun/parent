package com.gary.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.gary.web.result.Result;
import com.gary.web.util.I18nUtil;

@Controller
public class LanguageController extends BaseController {
	@RequestMapping("i18n")
	public @ResponseBody Result handleLocal(HttpServletRequest request, ModelMap model, String lang){
		Result result = new Result();
		Locale locale = getLocale(lang);
		if(locale != null){
			request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		}
		locale = getLocale(request);
		model.addAttribute("lang", locale);
		result.setData(model);
		result.setSuccess(true);
		return result;
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
