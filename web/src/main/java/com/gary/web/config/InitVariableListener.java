package com.gary.web.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

/**
 * 多域名配置文件加载器
 * @ClassName: InitVariableListener 
 * @Description: 与common/commonUrl.jsp配合使用,则自动匹配当前域名。<br>
 * 属性文件：variable.properties的情况下,会把配置加载到默认(如果有其他域名配置,<br>
 * 该参数会被覆盖),域名的属性文件: variable-company.properties,<br>
 * 可在web.xml设置contxt参数:VariableFilePath:配置文件路径(可使用通配符)<br>
 * @author zhouxianjun(Gary)
 * @date 2014-9-25 下午5:36:50 
 *
 */
public class InitVariableListener implements ServletContextListener {

	Logger log = LoggerFactory.getLogger(getClass());
	private ServletContext context = null;
	private String variableFilePath = "classpath*:variable*.properties";
	private final String defaultDomain = "localhost";
	
	public void contextDestroyed(ServletContextEvent arg0) {
		context = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void contextInitialized(ServletContextEvent arg0) {
		context = arg0.getServletContext();
		String initVariableFilePath = context.getInitParameter("VariableFilePath");
		if(!StringUtils.isEmpty(initVariableFilePath)){
			variableFilePath = initVariableFilePath;
		}
		//存放所有域名的配置
		Map<String, HashMap<String, String>> domainVariable = new HashMap<String, HashMap<String, String>>();
		try {
			ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
			//获取所有通配符变量配置文件
			Resource[] resources = patternResolver.getResources(variableFilePath);
			for (Resource resource : resources) {
				String filename = resource.getFilename();
				String domain = defaultDomain;
				try {
					int indexOf = filename.indexOf("-");
					if(indexOf != -1){
						domain = filename.substring(indexOf + 1, filename.lastIndexOf("."));
					}
				} catch (Exception e) {}
				HashMap<String, String> value = new HashMap<String, String>((Map) PropertiesLoaderUtils.loadAllProperties(filename));
				domainVariable.put(domain, value);
				log.info("环境变量[" + domain + "]-->" + value);
			}
			context.setAttribute("domainVariable", domainVariable);
			context.setAttribute("defaultDomain", defaultDomain);
			domainVariable = null;
		} catch (IOException e) {
			log.error("初始化环境变量失败: " + e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getVariableMap(HttpServletRequest request) {
		String domain = request.getServerName();
		ServletContext context = request.getSession().getServletContext();
		HashMap<String, HashMap<String, String>> domainVariable = (HashMap<String, HashMap<String, String>>)context.getAttribute("domainVariable");
		String defaultDomain = (String)context.getAttribute("defaultDomain");
		//截取获得域名
		if(!StringUtils.isEmpty(domain)){
			String[] tmpDomain = domain.split("\\.");
			if(tmpDomain != null && tmpDomain.length > 1){
				domain = tmpDomain[1];
			}
		}
		if(StringUtils.isEmpty(domain)){
			domain = defaultDomain;
		}
		//先把默认的域名配置放如map,后续如有相同的配置则覆盖默认的
		HashMap<String, String> map = domainVariable.get(defaultDomain);
		if(map == null){
			map = new HashMap<String, String>();
		}
		//之前已经放入过一次默认的了
		if(!defaultDomain.equals(domain) && domainVariable.containsKey(domain)){
			HashMap<String, String> m = domainVariable.get(domain);
			if(m != null && !m.isEmpty()){
				map.putAll(m);
			}
		}
		return map;
	}
}
