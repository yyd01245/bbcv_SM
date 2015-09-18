package prod.nebula.vrc.module.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vrc.config.JSONConstants;
import prod.nebula.vrc.config.VODConst;
import prod.nebula.vrc.core.common.constants.ResConstants;
import prod.nebula.vrc.module.http.Constants;
import prod.nebula.vrc.module.resctrl.Controller;
import prod.nebula.vrc.module.resctrl.VODResCtrl;
import prod.nebula.vrc.service.TCPServer;
import prod.nebula.vrc.util.Commons;
import prod.nebula.vrc.util.ParamUtils;
import prod.nebula.vrc.util.StringUtil;

public class InvokeServlet extends HttpServlet {
	private static final long serialVersionUID = 520689948416156514L;
	public final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getRequestURI().toString();
		String serialNo = Commons.getSerialNo();
		logger.info("【VGW】 URL:" + url + serialNo);

		String response = "";

		try {
			// Get HTTP Parameters.
			String sessionId = ParamUtils.getParameter(req,
					ResConstants.sessionId);
			String begintime = ParamUtils.getParameter(req,
					ResConstants.beginTime);
			String jsonCallBack =  ParamUtils.getParameter(req,"jsoncallback");
			logger.info("=================jsonCallBack==>"+jsonCallBack);
			
			if (StringUtil.assertNotNull(sessionId)) {
				if (url.contains(Constants.PLAY)) {
					response = play(sessionId, serialNo);
				} else if (url.contains(Constants.RESUME)) {
					response = resume(sessionId, serialNo);
				}else if (url.contains(Constants.PAUSE)) {
					response = pause(sessionId, serialNo);
				} else if (url.contains(Constants.FORWARD)) {
					response = forward(sessionId, serialNo);
				} else if (url.contains(Constants.BACKWARD)) {
					response = backward(sessionId, serialNo);
				}else if (url.contains(Constants.CHOOSETIME)) {
					response = choosetime(sessionId,serialNo,begintime);
				}else if (url.contains(Constants.GETTIME)) {
					response = getTime(sessionId,serialNo);
				}else{
					response = test();
				}
			} else {
				logger.error("【VGW】param error sessionid is null" + serialNo);
				response = Constants.SESSIONIDEMPTY_MSG;
			}
		} catch (Exception e) {
			response = Constants.INTERNAL_ERROR_MSG;
		} finally {
			try {
				resp.setContentType("text/html;charset=UTF-8");
				PrintWriter out = resp.getWriter();
				out.print(response);
				out.flush();
				out.close();
//				
//				OutputStream out = resp.getOutputStream();
//				out.write(response.getBytes());
//				out.flush();
//				out.close();
			} catch (Exception e) {
				logger.error("【VGW】send response error" + serialNo, e);
			}
		}

	}
	
	private String test(){
		String a="";
		//a= "<!DOCTYPE><html><head><script>parent.window.setData(\"{'currenttime':'1222666222.000000','totaltime':'7822222.000000'}\");</script></head><body></body></html>";
		a = "var json = \"{'currenttime':'1222666222.000000','totaltime':'7822222.000000'}\"; ";
		return a;
	}

	//获取播放时间
	private String getTime(String sessionId, String serialNo) {
		Controller cdnCtrl = VODResCtrl.getInstance().getCtrl(sessionId);
		JSONObject retObj = new JSONObject();
		retObj.put(JSONConstants.CURRENTTIME, cdnCtrl.getCurrentTime());
		retObj.put(JSONConstants.TOTALTIME, cdnCtrl.getTotalTime());
		String data = retObj.toString();
		return "var json=\'"+data+"\'";
	}
	
	
	//暂停后继续播放
	private String resume(String sessionId, String serialNo) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put(ResConstants.serialNo, serialNo);
		String response = Constants.OK;
		try {
			Controller cdnCtrl = VODResCtrl.getInstance().getCtrl(sessionId);
			VODResCtrl.getInstance()
					.VODOperate(VODConst.KEYVALUE_RESUME, cdnCtrl);
			
			response = retRespose(TCPServer.getControllerList().get(sessionId));
		} catch (Exception e) {
			response = Constants.INTERNAL_ERROR_MSG;
		} finally {
		}
		return response;
	}
	
	private String play(String sessionId, String serialNo) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put(ResConstants.serialNo, serialNo);
		String response = Constants.OK;
		try {
			Controller cdnCtrl = VODResCtrl.getInstance().getCtrl(sessionId);
			VODResCtrl.getInstance()
					.VODOperate(VODConst.KEYVALUE_PLAY, cdnCtrl);
			
			response = retRespose(TCPServer.getControllerList().get(sessionId));
		} catch (Exception e) {
			response = Constants.INTERNAL_ERROR_MSG;
		} finally {
		}
		return response;
	}

	private String pause(String sessionId, String serialNo) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put(ResConstants.serialNo, serialNo);
		String response = Constants.OK;
		try {
			Controller cdnCtrl = VODResCtrl.getInstance().getCtrl(sessionId);
			VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_PAUSE,
					cdnCtrl);
			response = retRespose(TCPServer.getControllerList().get(sessionId));
			
		} catch (Exception e) {
			response = Constants.INTERNAL_ERROR_MSG;
		} finally {
		}
		return response;
	}
	
	//快进操作
	private String forward(String sessionId, String serialNo) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put(ResConstants.serialNo, serialNo);
		String response = Constants.OK;
		try {
			Controller cdnCtrl = VODResCtrl.getInstance().getCtrl(sessionId);
			VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_FAST,
					cdnCtrl);
		} catch (Exception e) {
			response = Constants.INTERNAL_ERROR_MSG;
		} finally {
		}
		return response;
	}
	
	//快退操作
	private String backward(String sessionId, String serialNo) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put(ResConstants.serialNo, serialNo);
		String response = Constants.OK;
		try {
			Controller cdnCtrl = VODResCtrl.getInstance().getCtrl(sessionId);
			VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_SLOW,
					cdnCtrl);
		} catch (Exception e) {
			response = Constants.INTERNAL_ERROR_MSG;
		} finally {
		}
		return response;
	}
	
	//选择时间播放
	private String choosetime(String sessionId, String serialNo,String beginTime) {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put(ResConstants.serialNo, serialNo);
		String response = Constants.OK;
		try {
			Controller cdnCtrl = VODResCtrl.getInstance().getCtrl(sessionId);
			cdnCtrl.setBeginTime(beginTime);
			
			cdnCtrl.setCurrentTime(beginTime);
			VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_CHOOSETIME,
					cdnCtrl);
			
			response = retRespose(TCPServer.getControllerList().get(sessionId));
		} catch (Exception e) {
			response = Constants.INTERNAL_ERROR_MSG;
		} finally {
		}
		return response;
	}

	
	public static void main(String args[]){
		JSONObject retObj = new JSONObject();
		retObj.put(JSONConstants.CURRENTTIME, "111");
		retObj.put(JSONConstants.TOTALTIME, "3333");
		System.out.println(retObj.toString());
	}
	
	private String retRespose(Controller cdnCtrl){
		JSONObject retObj = new JSONObject();
		retObj.put(JSONConstants.CURRENTTIME, cdnCtrl.getCurrentTime());
		retObj.put(JSONConstants.TOTALTIME, cdnCtrl.getTotalTime());
		return retObj.toString();
	}
}
