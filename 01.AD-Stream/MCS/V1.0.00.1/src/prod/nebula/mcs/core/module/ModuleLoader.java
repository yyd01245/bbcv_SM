package prod.nebula.mcs.core.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.mcs.core.CoreLoader;
import prod.nebula.mcs.core.common.CommConstants;
import prod.nebula.mcs.core.common.ResConstants;
import prod.nebula.mcs.core.executor.RedisInterface;
import prod.nebula.mcs.util.ApplicationContextHelper;

public class ModuleLoader {
	public final Logger logger = LoggerFactory.getLogger(ModuleLoader.class);
	
	public ModuleLoader(){
		try {
			CoreLoader core = (CoreLoader) ApplicationContextHelper
					.getBean(ResConstants.CORE_MODULE_NAME);
			RedisInterface redisImpl = (RedisInterface) ApplicationContextHelper
					.getBean("redisImpl");
			
			RedisInterface cscsredisImpl = (RedisInterface) ApplicationContextHelper
			.getBean("cscsredisImpl");

			core.setMtpmdbInterface(redisImpl);
			core.setCscsmdbInterface(cscsredisImpl);
		} catch (Exception e) {
			logger.info("【REDIS】初始化服务失败");
			logger.error("[REDISModuleLoader]module:"
					+ CommConstants.REDIS_MODULE_NAME + " load failed", e);
			System.exit(0);
		}
	}

}
