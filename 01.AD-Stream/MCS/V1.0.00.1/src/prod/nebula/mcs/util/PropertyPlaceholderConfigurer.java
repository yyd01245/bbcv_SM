package prod.nebula.mcs.util;

import prod.nebula.framework.mina.socket.JsonNioSocketAcceptor;
import prod.nebula.mcs.core.CoreLoader;




public class PropertyPlaceholderConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {


	public PropertyPlaceholderConfigurer(CoreLoader core) {
		super();
		this.setProperties(core.getProperties());
	}
	public PropertyPlaceholderConfigurer() {
		super();
		CoreLoader core = (CoreLoader) ApplicationContextHelper.getBean("core");
		this.setProperties(core.getProperties());
		JsonNioSocketAcceptor acceptor = (JsonNioSocketAcceptor) ApplicationContextHelper
		.getBean("cumsMinaAcceptor");
//		acceptor.getSessionConfig().setReadBufferSize(9216);
	}	
}
