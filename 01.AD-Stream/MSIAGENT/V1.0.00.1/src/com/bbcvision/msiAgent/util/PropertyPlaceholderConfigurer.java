package com.bbcvision.msiAgent.util;

import com.bbcvision.msiAgent.core.CoreLoader;



public class PropertyPlaceholderConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	public PropertyPlaceholderConfigurer(CoreLoader core) {
		super();
		this.setProperties(core.getProperties());
	}	
}
