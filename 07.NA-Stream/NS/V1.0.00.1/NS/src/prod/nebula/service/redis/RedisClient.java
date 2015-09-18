/**  
 * 类名称：RedisClient 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-15 下午04:19:45 
 */
package prod.nebula.service.redis;

import redis.clients.jedis.Jedis;

/**    
 * 类名称：RedisClient   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-15 下午04:19:45    
 * 备注：   
 * @version    
 *    
 */
public class RedisClient {
	
   public Jedis jedis = new Jedis("127.0.0.1",6379);
   	 
	  public RedisClient(){
	       
	        jedis.auth("1234");//验证密码	        
	   
	  }

}
