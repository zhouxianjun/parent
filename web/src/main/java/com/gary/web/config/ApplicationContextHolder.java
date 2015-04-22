package com.gary.web.config;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextHolder implements ApplicationContextAware {
	public Logger logger = Logger.getLogger(ApplicationContextHolder.class);
	private static ApplicationContext applicationContext;
	private static String defaultApplicationContext = "classpath:applicationContext*.xml";
	public void setApplicationContext(ApplicationContext context) throws BeansException {
        if(applicationContext != null) {
            throw new IllegalStateException("ApplicationContextHolder already holded 'applicationContext'.");
        }
        applicationContext = context;
        logger.info("holded applicationContext,displayName:"+applicationContext.getDisplayName());
    }
     
    public static ApplicationContext getApplicationContext() {
    	 if(applicationContext == null){
         	applicationContext = new ClassPathXmlApplicationContext(defaultApplicationContext);
         }
         if(applicationContext == null)
            throw new IllegalStateException("'applicationContext' property is null,ApplicationContextHolder not yet init.");
        return applicationContext;
    }
     
    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }
    
    public static <T> T getBean(String beanName, Class<T> beanClass){
    	return getApplicationContext().getBean(beanName, beanClass);
    }
     
    public static void cleanHolder() {
        applicationContext = null;
    }

	public void setDefaultApplicationContext(String defaultApplicationContext) {
		ApplicationContextHolder.defaultApplicationContext = defaultApplicationContext;
	}

}
