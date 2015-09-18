package prod.nebula.vgw4sida.util;

import prod.nebula.vgw4sida.core.CoreLoader;



public class PropertyPlaceholderConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	public PropertyPlaceholderConfigurer(CoreLoader core) {
		super();
		this.setProperties(core.getProperties());
	}	
}
