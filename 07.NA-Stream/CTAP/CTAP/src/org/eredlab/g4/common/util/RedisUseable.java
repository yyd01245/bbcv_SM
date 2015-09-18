package org.eredlab.g4.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class RedisUseable {
	Logger log = LoggerFactory.getLogger(RedisUseable.class);
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
