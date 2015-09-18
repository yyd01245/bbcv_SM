package prod.nebula.vgw.util;

import prod.nebula.vgw.core.CoreLoader;



public class PropertyPlaceholderConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	public PropertyPlaceholderConfigurer(CoreLoader core) {
		super();
		this.setProperties(core.getProperties());
	}	
}
