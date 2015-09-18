package prod.nebula.commons.spring;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ApplicationContextHelper implements ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextHelper.class);
	public static ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		ApplicationContextHelper.context = context;
	}
	
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> requiredType){
		return context.getBean(requiredType);
	}
	
	public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
		Map<String, T> map = context.getBeansOfType(clazz);
		if(null == map || map.isEmpty()) {
			logger.warn("bean collection of [" + clazz + "] is empty.");
			return null;
		} else {
			return map;
		}
	}
	}
	
