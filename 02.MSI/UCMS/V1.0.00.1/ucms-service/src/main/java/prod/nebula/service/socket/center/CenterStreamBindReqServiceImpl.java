package prod.nebula.service.socket.center;

import java.io.IOException;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import prod.nebula.commons.enums.msi.MgwEnum;
import prod.nebula.commons.string.OidUtils;
import prod.nebula.exception.msi.BbcvMsiException;

import com.bbcv.mdb.util.StringUtil;

@Service("msStreamBindReqService")
public class CenterStreamBindReqServiceImpl {
	@Resource(name="centerService",type=CenterServiceImpl.class)
	private CenterServiceImpl centerService;
	
	private static final Logger logger=Logger.getLogger(CenterStreamBindReqServiceImpl.class);
	
	public String userBindStream(String username,String token,String stream_id,String recall_addr) throws BbcvMsiException, IOException{
		String serialno = OidUtils.newSerialno(32);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cmd", "stream_bind");
		jsonObject.put("username", username);
		jsonObject.put("token", token);
		jsonObject.put("stream_id", stream_id);
		jsonObject.put("recall_addr", recall_addr);
		jsonObject.put("serialno", serialno);
		String sendStr = jsonObject.toString();
		String retString = centerService.send(sendStr);
		logger.info("[ucms-userBindStream-ms]:resp===="+retString);
		if(StringUtil.assertNotNull(retString)){
			retString = retString.replaceAll(MgwEnum.SUFFIX.getDesc(), "");
			return retString;
		}else{
			return null;
		}
	}
	public String userUnBindStream(String username,String stream_id) throws BbcvMsiException, IOException{
		String serialno = OidUtils.newSerialno(32);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cmd", "stream_unbind");
		jsonObject.put("username", username);
		jsonObject.put("stream_id", stream_id);
		jsonObject.put("serialno", serialno);
		String sendStr = jsonObject.toString();
		String retString = centerService.send(sendStr);
		logger.info("[ucms-userBindStream-ms]:resp===="+retString);
		if(StringUtil.assertNotNull(retString)){
			retString = retString.replaceAll(MgwEnum.SUFFIX.getDesc(), "");
			return retString;
		}else{
			return null;
		}
	}
}
