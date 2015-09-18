/**  
 * 类名称：SendClientUtil 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2015-1-16 上午09:48:38 
 */
package org.eredlab.g4.urm.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.cabs.web.AppGroupManagerAction;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.client.IOSocketClient;
import org.eredlab.g4.common.util.client.TcpClient;

/**   
 * 类名称：SendClientUtil   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2015-1-16 上午09:48:38   
 * 备注：   
 * @version    
 *    
 */
public class SendClientUtil {
	private static Log log = LogFactory.getLog(AppGroupManagerAction.class);
	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param args    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static void main(String[] args) {
	
		
		
		Dto queryDto = new BaseDto();
		Dto retDto = new BaseDto() ;
		queryDto.put("cmd", "configquery");
		queryDto.put("appname", "cag");
		
		String queryStr = JsonHelper.encodeObject2Json(queryDto);
		String queryString = queryStr + "XXEE";
		
	    System.out.println("queryStr="+queryStr);
	    
	    System.out.println("queryString="+queryString);	   
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		
		
		TcpClient client = new TcpClient();

		String returnStr = client.sendStr("192.168.100.56", 30191, 5000, null, queryString,codecFactory);
		System.out.println("接收到的报文："+returnStr);

	    String	revStr = returnStr.replace("XXEE", "");
	

		if (!(revStr == null || "".equals(revStr))) {
			String JsonStr = JsonHelper.encodeObject2Json(revStr);
			Dto dto = JsonHelper.parseSingleJson2Dto(JsonStr);
			if(dto.getAsInteger("retcode")<0){
				retDto.put("success", new Boolean(false));
			}else{
				retDto.put("success", new Boolean(true));
			}
		}else{
			retDto.put("success", new Boolean(false));
		}
		
		
	

	}

}
