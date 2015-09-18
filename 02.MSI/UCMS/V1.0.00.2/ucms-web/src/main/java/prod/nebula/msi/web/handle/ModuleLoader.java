package prod.nebula.msi.web.handle;

import java.util.Properties;
public abstract class ModuleLoader {
	protected Properties properties;

	public abstract Properties loadContants(String moduleName);

	
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
