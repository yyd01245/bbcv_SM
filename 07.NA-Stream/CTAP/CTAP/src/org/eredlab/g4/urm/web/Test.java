/**  
 * 类名称：Test 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2015-1-22 下午01:11:22 
 */
package org.eredlab.g4.urm.web;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**   
 * 类名称：Test   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2015-1-22 下午01:11:22   
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


		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		
		JSONArray array1 = new JSONArray();
		
		JSONArray array2 = new JSONArray();
	
        obj1.put("cmd", "configupdate");
        obj1.put("add", "192.168.100.11");
        
        obj2.put("config", "ke1");
        array2.add(obj2);
        obj1.put("app",array2);
        
       System.out.println(obj1.toString()); 
        
        
	}

}
