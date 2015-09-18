/**  
 * 类名称：Test 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-15 下午03:44:01 
 */
package prod.nebula.service.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

import redis.clients.jedis.Jedis;

/**    
 * 类名称：Test   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-15 下午03:44:01    
 * 备注：   
 * @version    
 *    
 */
public class Test {

	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param args    设定文件
	 * @return void    返回类型
	 * @throws
	 */
    public static void main(String[] args) {
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s =   sdf.format(new Date());
    	System.out.println(s);
    	
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.auth("1234");//验证密码
        
        String username="187";
        String username1="1871";
        
        jedis.set(username, s);
        jedis.set(username1, s);
       
        System.out.println(jedis.get(username)+"=username");
        System.out.println(jedis.get(username1)+"=username1");
        
    }

}
