package prod.nebula.socket.mina;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

public class DynamicLoadBean implements ApplicationContextAware {
	public final Logger logger = LoggerFactory.getLogger(getClass());
	private ConfigurableApplicationContext applicationContext = null;  
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {  
        this.applicationContext = (ConfigurableApplicationContext)applicationContext;  
    }  
    public ConfigurableApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
    
    public void loadBeanFromXML(StringBuffer beanXML){  
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)getApplicationContext().getBeanFactory());  
        beanDefinitionReader.setValidationMode(0);
        beanDefinitionReader.setResourceLoader(getApplicationContext());  
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));  
        try {
        	ByteArrayInputStream is = new ByteArrayInputStream(beanXML.toString().getBytes("UTF-8"));
        	Resource inputResource = new InputStreamResource(is);
        	logger.debug("load bean number:"+beanDefinitionReader.loadBeanDefinitions(inputResource));
        } catch (BeansException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    } 
    public void loadBean(String configLocationString){  
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)getApplicationContext().getBeanFactory());  
        beanDefinitionReader.setResourceLoader(getApplicationContext());  
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));  
        try {  
            String[] configLocations = new String[]{configLocationString};  
            for(int i=0;i<configLocations.length;i++)  
                beanDefinitionReader.loadBeanDefinitions(getApplicationContext().getResources(configLocations[i]));  
        } catch (BeansException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  

}
