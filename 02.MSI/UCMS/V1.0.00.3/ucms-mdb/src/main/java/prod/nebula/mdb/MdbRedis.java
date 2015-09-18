/** 
 * Project: bbcvision3-mdb
 * author : PengSong
 * File Created at 2013-11-5 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.mdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.bbcv.mdb.redis.MDBInterface;

/** 
 * TODO Comment of MdbRedis 
 * 
 * @author PengSong 
 */
@Service("mdbredis")
public class MdbRedis {
	public static final Logger logger = LoggerFactory.getLogger(MdbRedis.class);
	
	private JedisPool jedisPool;
	
	private MDBInterface mdbImpl;
	
	public MDBInterface getMdbImpl() {
		return mdbImpl;
	}

	public <T> List<T> getPageListValue(String key, int start, int end,Class<T> c) {
		Jedis jedis = null;
		List<String> listStr = null;
		List<T> listCh = new ArrayList<T>();
		boolean borrowOrOprSuccess = true;
		try {
			if (StringUtils.isNotBlank(key)) {
				jedis = jedisPool.getResource();
				listStr = jedis.lrange(key, start, end);
				logger.info("listStr="+listStr);
				if (listStr !=null) {
					for (String ch : listStr) {
						if (StringUtils.isNotBlank(ch)) {
							T obj = JSONObject.parseObject(ch,c);
							listCh.add((T)obj);
						}
					}
				}
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
		return listCh;
	}
	
	public boolean addListValue(String key, String value) {
		Jedis jedis = null;
		boolean success = false;
		boolean borrowOrOprSuccess = true;

		try {
			if (StringUtils.isNotBlank(key)) {
				jedis = jedisPool.getResource();
				jedis.rpush(key, value);
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

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void setMdbImpl(MDBInterface mdbImpl) {
		this.mdbImpl = mdbImpl;
	}
}
