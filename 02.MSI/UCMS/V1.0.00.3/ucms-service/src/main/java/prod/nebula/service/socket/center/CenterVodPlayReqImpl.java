package prod.nebula.service.socket.center;

import java.io.IOException;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.commons.enums.msi.MgwEnum;
import prod.nebula.commons.string.OidUtils;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.mdb.MdbRedis;

import com.bbcv.mdb.redis.impl.MDBDataImpl;
import com.bbcv.mdb.util.StringUtil;

@Service("msVodPlayReqService")
public class CenterVodPlayReqImpl {
	@Resource(name="centerService",type=CenterServiceImpl.class)
	private CenterServiceImpl centerService;
	
	@Resource(name="mdbredis",type=MdbRedis.class)
	private MdbRedis mdbredis;
	
	private static final Logger logger=Logger.getLogger(CenterVodPlayReqImpl.class);
	
	public boolean userVodPlay(String username,String stream_id,String url,String vodname,String posterurl,String nickname) throws BbcvMsiException, IOException{
		String serialno = OidUtils.newSerialno(32);
		JSONObject jsonObject = new JSONObject();
		String user = (nickname==null||"".equals(nickname))?username:nickname;
		jsonObject.put("cmd", "vod_play");
		jsonObject.put("username", user);
		jsonObject.put("stream_id", stream_id);
		jsonObject.put("url", url);
		jsonObject.put("vodname", vodname);
		jsonObject.put("posterurl", posterurl);
		jsonObject.put("serialno", serialno);
		String sendStr = jsonObject.toString();
		String retString = centerService.send(sendStr);
		logger.info("[ucms-userVodPlay-ms]:resp===="+retString);
		if(StringUtil.assertNotNull(retString)){
			retString = retString.replaceAll(MgwEnum.SUFFIX.getDesc(), "");
			JSONObject retJson = JSONObject.fromObject(retString);
			String ret_code = retJson.getString("ret_code");
			if(ret_code=="0"||"0".equalsIgnoreCase(ret_code)){ //对VOD键值控制定义地址
				String key_addr = retJson.getString("key_addr");
				MDBDataImpl mdbDateImpl = (MDBDataImpl) mdbredis.getMdbImpl();
				mdbDateImpl.putString(UcmsConfig.VGW_KEY_SEND+"_"+username, key_addr);
//				logger.info("VOD键值上传地址："+mdbDateImpl.getString(UcmsConfig.VGW_KEY_SEND+"_"+username));
				mdbDateImpl.putString("LOGOUTVOD_"+username, "true");//用户按“退出”	键初始化；	
				mdbDateImpl.putString("VODNAME_"+username, vodname);//用户点播片名
				mdbDateImpl.putString("POSTERURL_"+username, posterurl);		//影片对应海报地址
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
