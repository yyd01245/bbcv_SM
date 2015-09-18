/**
 * 
 */
package prod.nebula.msi.web.handle;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.service.msi.impl.UserInfoServiceImpl;
import prod.nebula.service.socket.mgw.MgwKeyControlServiceImpl;


/**
 * 报文处理程序
 * @author RobinLau
 * 
 */
public class TcpPersistentHandler extends IoHandlerAdapter implements
		Serializable {
	private static final long serialVersionUID = 1L;
	public final Logger logger = LoggerFactory.getLogger(getClass());  
	
	@Resource(name="userInfoService",type=UserInfoServiceImpl.class)
	private UserInfoServiceImpl userInfoService;
	
	@Resource(name="mgwKeyControlService",type=MgwKeyControlServiceImpl.class)
	private MgwKeyControlServiceImpl mgwKeyControlService;
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
		InetAddress  address = null;
		String ip="";
		String port="";
   		try{
 			address = ((InetSocketAddress)session.getRemoteAddress()).getAddress();
 			ip = address.getHostAddress();
 			port = session.getRemoteAddress().toString().split(":")[1];
		}catch(Exception ex){
		}finally{
			try {
				if (null != session)
					session.close(false);
			} catch (Exception e) {
 			}
		}
		logger.error("【UCMS】["+ip+":"+port+"]:exceptionCaught");
	}
	/**
	 * 报文接收处理
	 * @param IoSession ioSession
	 * @param Object message
	 */
	public void messageReceived(IoSession ioSession, Object message) {
		long startTime = System.currentTimeMillis();
		InetAddress  address = null;
		String ip="";
		String port="";
		String seriNo="";
  		try{
  			address = ((InetSocketAddress)ioSession.getRemoteAddress()).getAddress();
 			ip = address.getHostAddress();
 			port = ioSession.getRemoteAddress().toString().split(":")[1];
		}catch(Exception ex){
		}
		try {
 			String revStr = message.toString();
//			ParameterHolder.add(revStr.toString());
 			logger.debug("【UCMS】tcp client receive ["+ip+":"+port+"] :" + revStr+"XXEE");
 			// 定义返回字符
			String returnMessage ="";
//			JSONObject json = JSONObject.fromObject(ParameterHolder.get());
			JSONObject json = JSONObject.fromObject(revStr);
			
			String com=json.get("cmd").toString();
			UcmsConfig.COMMAND cc = UcmsConfig.COMMAND.valueOf(com);
			String username = json.getString("username");
			switch (cc) {
			case get_session:
				JSONObject retJson = new JSONObject();
				retJson.put("cmd", com);
				retJson.put("serialno", json.getString("serialno"));
				UserInfo userinfo = userInfoService.getUserInfo(username);
				if(userinfo!=null){
					int isonline=0;
					if(userinfo.getUser_status()==3||userinfo.getUser_status().equals(3)){
						isonline=1;
					}
					retJson.put("ret_code", "0");
					retJson.put("isonline", isonline);
				}else{
					retJson.put("ret_code", "-1");
					retJson.put("isonline", "0");
					retJson.put("msg", "用户不存在！");
				}
				returnMessage = retJson.toString();
				break;
			case stream_unbind:
				JSONObject retjson = new JSONObject();
				retjson.put("cmd", com);
				retjson.put("serialno", json.getString("serialno"));
				if(userInfoService.checkUserInfo(username)){
					userInfoService.unBindUser(username);//更新数据库信息
					retjson.put("ret_code", "0");
				}else{
					retjson.put("ret_code", "-1");
					retjson.put("msg", "用户不存在！");
				}
				returnMessage = retjson.toString();
				break;	
			case vod_over:
				JSONObject vodjson = new JSONObject();
				vodjson.put("cmd", com);
				vodjson.put("serialno", json.getString("serialno"));
				if(userInfoService.checkUserInfo(username)){
					userInfoService.updateUserStatus(username, UcmsConfig.banding_status);//更新数据库信息
					vodjson.put("ret_code", "0");
				}else{
					vodjson.put("ret_code", "-1");
					vodjson.put("msg", "用户不存在！");
				}
				returnMessage = vodjson.toString();
				break;	
				
			case quit_timeover:
				JSONObject quitjson = new JSONObject();
				quitjson.put("cmd", com);
				quitjson.put("serialno", json.getString("serialno"));
				UserInfo userinfo2 = userInfoService.getUserInfo(username);
				mgwKeyControlService.keyControl(username, "0x1d","3",userinfo2.getStream_id(),userinfo2.getNickname());
				quitjson.put("ret_code", "0");
				returnMessage = quitjson.toString();
				break;	
			}
			logger.debug("【UCMS】tcp client return ["+ip+":"+port+"] :" + returnMessage +"XXEE"  );
			ioSession.write(returnMessage);
			long endTime = System.currentTimeMillis();
			logger.info("【UCMS】"+seriNo +"-整体耗时:"+ ( endTime -startTime )+"ms" );
		} catch (Exception e) {
			logger.error("【UCMS】 TCP Receive Exception,"+e) ;
		} finally{
			try {
				if (null != ioSession)
					ioSession.close(false);
			} catch (Exception e) {
				logger.error("【UCMS】"+seriNo+"-关闭连接失败");
			}
	    }
	}
}
