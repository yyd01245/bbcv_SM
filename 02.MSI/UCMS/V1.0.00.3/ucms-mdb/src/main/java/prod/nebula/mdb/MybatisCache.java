/** 
 * Project: bbcvision3-mdb
 * author : PengSong
 * File Created at 2013-11-12 
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

import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.commons.constans.MsiRedisKey;
import prod.nebula.commons.spring.ApplicationContextHelper;

import com.bbcv.mdb.redis.MDBInterface;
import com.bbcv.mdb.redis.impl.MDBDataImpl;

/** 
 * mybatis自定义缓存实现
 * 
 * @author PengSong 
 */
public class MybatisCache implements Cache{
	public static final Logger logger = LoggerFactory.getLogger(MybatisCache.class);
	
	private MDBInterface mdb = ApplicationContextHelper.getBean(MDBDataImpl.class);
	
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	private String id;
	
	private LinkedList<String> cacheKeys = new LinkedList<String>();
	
	public MybatisCache(String id) {
		logger.debug("[bbcvision3-mdb]MybatisRedisCache:id="+id);
	    this.id = id;
	}
	
    public String getId() {  
        return this.id;
    }
    
    public int getSize() {
    	return cacheKeys.size();
    }
    
    public void putObject(Object key, Object value) {
		String cacheKey = MsiRedisKey.getMybatisKey(key.hashCode());
		if(!mdb.putObject(cacheKey, value)) {
			logger.error("[bbcvision3-mdb]putObject error,value is null!:"+cacheKey+"="+value);
		} else {
			if (!cacheKeys.contains(cacheKey)){  
	            cacheKeys.add(cacheKey);  
	        }
			logger.debug("[bbcvision3-mdb]putObject:"+cacheKey+"="+value);
		}
    }
    
    public Object getObject(Object key) {
		String cacheKey = MsiRedisKey.getMybatisKey(key.hashCode());
		Object value = mdb.getObject(cacheKey);
		if (!cacheKeys.contains(cacheKey)){  
            cacheKeys.add(cacheKey);  
        } 
		if(null != value) {
			logger.debug("[bbcvision3-mdb]getObject:"+cacheKey+"="+value);
			return value;
		} else {
			logger.warn("[bbcvision3-mdb]getObject("+cacheKey+"):value is null");
			return null;
		}
    }
    
    public Object removeObject(Object key) {
    	String cacheKey = MsiRedisKey.getMybatisKey(key.hashCode());
    	Object value = mdb.getObject(cacheKey);
    	if(!mdb.removeObject(cacheKey)) {
    		logger.error("[bbcvision3-mdb]removeObject error!:"+cacheKey+"="+value);
    	} else {
    		cacheKeys.remove(cacheKey); 
    		logger.debug("[bbcvision3-mdb]removeObject :"+cacheKey+"="+value);
    	}
    	return value;
    }
    
    public void clear() { 
    	logger.debug("[bbcvision3-mdb]clear...");
    	for(String cacheKey : cacheKeys) {
    		logger.debug("[bbcvision3-mdb]clear:"+cacheKey);
    		if(!mdb.removeObject(cacheKey)) {
        		logger.error("[bbcvision3-mdb]removeObject error!:"+cacheKey);
        	}
    	}
    	cacheKeys.clear();
    }
    
    public ReadWriteLock getReadWriteLock() {
    	return readWriteLock;
    }
}
