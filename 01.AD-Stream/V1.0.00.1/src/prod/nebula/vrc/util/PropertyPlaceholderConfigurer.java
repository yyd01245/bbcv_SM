package prod.nebula.vrc.util;

import prod.nebula.vrc.core.CoreLoader;



public class PropertyPlaceholderConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	public PropertyPlaceholderConfigurer(CoreLoader core) {
		super();
		this.setProperties(core.getProperties());
	}	
}
