package prod.nebula.msi.web.handle;


import java.util.Properties;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import prod.nebula.commons.config.UcmsConfig;


public class UCMSModuleLoader  extends ModuleLoader{
	public final Logger logger = LoggerFactory.getLogger(getClass());
	private IoSession iosession;
	private ApplicationContext atx;
	public IoSession getIosession() {
		return iosession;
	}
	
	public ApplicationContext getAtx() {
		return atx;
	}
	
	
	public UCMSModuleLoader() {
		try {
			this.properties = this.loadContants("context");
			
			ApplicationContext atx = new ClassPathXmlApplicationContext(
					"context/applicationContext-mina.xml");
			this.atx = atx;
			
			
//			DynamicLoadBean dynamicBeanLoad = new DynamicLoadBean();
//			dynamicBeanLoad.setApplicationContext(atx);
			
//			dynamicBeanLoad.loadBeanFromXML(this.getTcpServerXML(UcmsConfig.local_server_port));
			NioSocketAcceptor acceptor = (NioSocketAcceptor)atx.getBean("ucmsMinaAcceptor");
			acceptor.getSessionConfig().setReadBufferSize(8192);
			
			//UDP����
/*			//dynamicBeanLoad.loadBeanFromXML(this.getUdpServerXML(udpServerPort));
			NioDatagramAcceptor udpAcceptor = (NioDatagramAcceptor)atx.getBean("udpMtpMinaAcceptor");
	
			//udpAcceptor.getSessionConfig().setReadBufferSize(8192);
*/			
			
		} catch (Exception e) {
			logger.info("[UCMSModuleLoader]module: applicationContext-mina.xml load failed", e.getMessage());
		}
	}
	
	

	@Override
	public Properties loadContants(String moduleName) {
		Properties properties = null;
		try {
//			properties = PropertiesUtil.getInstance().getProperties(moduleName);
//			this.properties = properties;
		} catch (Exception e) {
			logger.info("[MTAPModuleLoader]loadContants", e.getMessage());
		}
		return properties;
	}

	
	private StringBuffer getTcpServerXML(String port){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<beans");
		sb.append("	xmlns=\"http://www.springframework.org/schema/beans\"");
		sb.append("	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append("	xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd\">");
		sb.append("	<!-- The IoAcceptor which binds to port 1235 server side -->");
		sb.append("<bean id=\"mtpMinaAcceptor\" class=\"org.apache.mina.transport.socket.nio.NioSocketAcceptor\" init-method=\"bind\" destroy-method=\"unbind\"> "); 
		sb.append("	<property name=\"defaultLocalAddress\" value=\":"+port+"\" /> "); 
		sb.append("	<property name=\"handler\" ref=\"magMinaHandler\" /> "); 
		sb.append("	<property name=\"reuseAddress\" value=\"true\" /> "); 
		sb.append("	<property name=\"filterChainBuilder\" ref=\"mtpFilterChainBuilder\" />"); 
		sb.append("</bean> "); 	
		

		sb.append("</beans>");
		return sb;
	}
	

	
}
