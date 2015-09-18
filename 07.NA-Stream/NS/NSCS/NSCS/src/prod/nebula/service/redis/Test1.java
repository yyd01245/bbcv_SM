/**  
 * 类名称：Test 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-15 下午03:44:01 
 */
package prod.nebula.service.redis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

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
public class Test1 {

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

   String key = "13305731111"+"vodid";
   
        System.out.println(jedis.exists(key));
        System.out.println(jedis.get(key));
        
        
        String key1 = "13887654321vodidkey";
        
        System.out.println("------"+jedis.get(key1));
        
        jedis.del(key1);
        
        System.out.println("------"+jedis.get(key1));
        
        System.out.println("------"+jedis.exists(key1));
        
//        System.out.println(jedis.get(username1)+"=username1");
//
//        
//        System.out.println("系统中删除username: "+jedis.del("username"));
//        System.out.println("判断username是否存在："+jedis.exists("username"));
//        // 设置 key001的过期时间
//        System.out.println("设置 username的过期时间为5秒:"+jedis.expire("key001", 5));   
        
    }

}
