package org.eredlab.g4.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

public class RedisUseable {
	public static final Log log = LogFactory.getLog(RedisUseable.class);
	public boolean jedisUseable(String host,int port,String passwd){
		boolean isable =false;
		Jedis jedis = null; 
		try {
			jedis = new Jedis(host, port);
 			if(passwd!=null && !"".equals(passwd)){
				jedis.auth(passwd);
			}
			String str = jedis.echo("hello");
			if(str.length()>0){
				isable=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isable=false;
			log.info("判断redis是否可用返回错误信息"+e.getMessage());
		}finally{
			if(null!=jedis){
				jedis.disconnect();
			}
		}
		return isable;
	}

}
