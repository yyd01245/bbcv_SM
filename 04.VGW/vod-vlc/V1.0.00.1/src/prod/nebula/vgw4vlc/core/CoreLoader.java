package prod.nebula.vgw4vlc.core;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4vlc.util.PropertiesUtil;
import prod.nebula.vgw4vlc.util.StringUtil;

public class CoreLoader {
	public final Logger logger = LoggerFactory.getLogger(getClass());

	private Properties properties;
//	private CacheManager tmpCacheManager;
//	private Cache tmpCache;

	public Properties getProperties() {
		return properties;
	}

	public CoreLoader() {
		try {
			// 初始化配置参数
			this.properties = this.loadContantsFromClassPath("config.properties");
			
			// 初始化ehcache信息
//			CacheManager manager = new CacheManager(this.getClass()
//					.getClassLoader().getResourceAsStream("ehcache.xml"));
//			this.tmpCacheManager = manager;
//			this.tmpCache = manager.getCache("tmpDefault");

			// 初始化注入接口
			logger.info("************** [CORE]module: CoreLoader load success **************");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(
					"************** [CORE]module: CoreLoader load fail **************",
					e);
			System.exit(0);
		}
	}

	public Properties loadContants(String path) {
		Properties properties = null;
		try {
			properties = PropertiesUtil.getInstance().getPropertiesWithPath(
					path);
			this.printParms(properties);
		} catch (Exception e) {
			logger.error("[CORE] [CoreLoader]loadContants", e.getMessage());
		}
		return properties;
	}

	public Properties loadContantsFromClassPath(String path) {
		Properties properties = null;
		try {
			properties = PropertiesUtil.getInstance().getPropertiesWithPath(path);
		} catch (Exception e) {
			logger.error("[CORE] [CoreLoader]loadContants", e.getMessage());
		}
		return properties;
	}

	@SuppressWarnings("rawtypes")
	private void printParms(Properties properties) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append("----------The server configuration----------");
		sb.append("");
		Iterator itr = properties.entrySet().iterator();
		while (itr.hasNext()) {
			Entry e = (Entry) itr.next();
			if (!StringUtil.assertNotNull((String) e.getValue())) {
				continue;
			}
			sb.append(e.getKey() + ":" + e.getValue());
			sb.append("");
		}
		sb.append("--------------------------------------------");
		logger.info("[CORE] " + sb.toString() + "");
	}
}
