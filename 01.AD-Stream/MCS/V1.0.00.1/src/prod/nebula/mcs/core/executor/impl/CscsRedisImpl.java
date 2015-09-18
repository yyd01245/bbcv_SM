package prod.nebula.mcs.core.executor.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.cums.module.cscs.common.StringUtil;
import prod.nebula.mcs.core.CoreLoader;
import prod.nebula.mcs.core.common.ResConstants;
import prod.nebula.mcs.core.executor.RedisInterface;
import prod.nebula.mcs.util.ApplicationContextHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//重新检查一下各种操作的返回，现在的新增或删除都是成功没有对返回错误的处理，
//另外作为工具类，要允许传入参数数据异常
public class CscsRedisImpl implements RedisInterface {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	CoreLoader core = (CoreLoader) ApplicationContextHelper
			.getBean(ResConstants.CORE_MODULE_NAME);

	private JedisPool jedisPool;

	public CscsRedisImpl() throws Exception {

		String redisPasswd = core.getConfig().getCscsRedisPass();

		if (StringUtil.assertNotNull(redisPasswd)) {
			// jedisPool = new JedisPool(new JedisPoolConfig(), CoreLoader
			// .getConfig().getMtpRedisIP(), CoreLoader.getConfig()
			// .getMtpRedisPORT(), 2000, CoreLoader.getConfig()
			// .getMtpRedisPORT());
			jedisPool = new JedisPool(new JedisPoolConfig(), core.getConfig()
					.getCscsRedisIP(), core.getConfig().getCscsRedisPORT(), 2000,
					core.getConfig().getCscsRedisPass());
		} else if (redisPasswd.equals("")) {
			jedisPool = new JedisPool(new JedisPoolConfig(), CoreLoader
					.getConfig().getCscsRedisIP(), CoreLoader.getConfig()
					.getCscsRedisPORT());
		}

		if (jedisPool == null) {
			logger.error("[RedisDataImpl] Redis Initial Error");
			throw new Exception("[RedisDataImpl] Redis Initial Error");
		}
		this.jedisPool = jedisPool;

		jedisPool.getResource().set("MGW_RedisPD", redisPasswd);
	}

	public boolean addListValue(String key, List<String> value) {
		return false;
	}

	public boolean addValue(String key, String value) {
		Jedis jedis = null;
		boolean success = false;
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtil.assertNotNull(key)) {
				jedis = jedisPool.getResource();
				jedis.set(key, value);
				success = true;
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Put Key-Value Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return success;
	}

	public boolean addValueForLock(String key, int timeoutTime, String value) {

		return false;
	}

	public boolean delValue(String key) {
		Jedis jedis = null;
		boolean success = false;
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtil.assertNotNull(key)) {
				jedis = jedisPool.getResource();
				if (jedis.del(key) >= 0) {
					success = true;
				}
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Remove Key Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return success;
	}

	public boolean delValueForLock(String key) {
		return false;
	}

	public boolean flushAll() {
		Jedis jedis = null;
		boolean success = false;
		boolean borrowOrOprSuccess = true;
		try {
			jedis = jedisPool.getResource();
			jedis.flushAll();
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Flush All Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return success;
	}

	public List<String> getListValue(String key) {
		return null;
	}

	public String getValue(String key) {
		Jedis jedis = null;
		String ret = "";
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtil.assertNotNull(key)) {
				jedis = jedisPool.getResource();
				ret = jedis.get(key);
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Get Value Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return ret;
	}

	public String getValueForLock(String key) {
		return null;
	}

	public boolean repListValue(String key, List<String> value) {
		return false;
	}

	public boolean repValue(String key, String value) {
		return false;
	}

	public boolean addValue(String key, int timeoutTime, String value) {
		Jedis jedis = null;
		boolean success = false;
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtil.assertNotNull(key)) {
				jedis = jedisPool.getResource();
				jedis.set(key, value);
				success = true;
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Put Key-Value Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return success;
	}

	public boolean addSetValue(String key, String value) {
		Jedis jedis = null;
		boolean success = false;
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtil.assertNotNull(key)) {
				jedis = jedisPool.getResource();
				if (jedis.sadd(key, value) >= 0) {
					success = true;
				}
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Add Set Value Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return success;
	}

	public Set<String> getSetValue(String key) {
		Jedis jedis = null;
		Set<String> set = null;
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtil.assertNotNull(key)) {
				jedis = jedisPool.getResource();
				set = jedis.smembers(key);
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Get Set Values Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return set;
	}

	public boolean removeSetValue(String key, String value) {
		Jedis jedis = null;
		boolean success = false;
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtil.assertNotNull(key)) {
				jedis = jedisPool.getResource();
				if (jedis.srem(key, value) >= 0) {
					success = true;
				}
			}
		} catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			logger.error("[RedisDataImpl] Delete Set Value Error!", e);
		} finally {
			if (borrowOrOprSuccess) {
				jedisPool.returnResource(jedis);
			}
		}
		return success;
	}

	public Object getObject(String key) {
		return null;
	}

}
