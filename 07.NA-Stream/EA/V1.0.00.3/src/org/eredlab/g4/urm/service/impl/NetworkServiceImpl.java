package org.eredlab.g4.urm.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.urm.service.NetworkService;
import org.eredlab.g4.urm.util.Contents;
import org.eredlab.g4.urm.util.SendMessageClient;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class NetworkServiceImpl extends BaseServiceImpl implements	NetworkService {
	
	private static Log log = LogFactory.getLog(NetworkServiceImpl.class);
	
	public Dto saveNetworks(Dto pDto){
		Dto outDto = new BaseDto();
		
		int sum = pDto.getAsInteger("sum");
		String strNetRegionName = pDto.getAsString("strNetRegionName");
		String iNavgationStreamNum = pDto.getAsString("iNavgationStreamNum");
		String sdiNavgationStreamNum = pDto.getAsString("sdiNavgationStreamNum");
		String iAdvertisementStreamNum = pDto.getAsString("iAdvertisementStreamNum");
		String sdiAdvertisementStreamNum = pDto.getAsString("sdiAdvertisementStreamNum");
		String strNetworkComment = pDto.getAsString("strNetworkComment");
		
		
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			pDto.put("strNetRegionName", strNetRegionName);
			pDto.put("iNavgationStreamNum", iNavgationStreamNum);
			pDto.put("sdiNavgationStreamNum", sdiNavgationStreamNum);
			pDto.put("iAdvertisementStreamNum", iAdvertisementStreamNum);
			pDto.put("sdiAdvertisementStreamNum", sdiAdvertisementStreamNum);
			pDto.put("strNetworkComment", strNetworkComment);
			pDto.put("state", 0);
			
			urmDao.insert("Network.saveNetwork", pDto);	
//			
//			
//			  SendMessageClient s = new SendMessageClient(Contents.NETWORK,Contents.INSERT,null,"new");  
//			  s.send();
			outDto.put("success", TRUE);
		}
		
		return outDto;
	}
	
	public Dto deleteNetwork(Dto inDto) {
		Dto dto = new BaseDto();
		Dto outputDto = new BaseDto();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			String strNetRegionNum = arrChecked[i];
			dto.put("strNetRegionNum", strNetRegionNum);
			Integer NavCount = (Integer) urmDao.queryForObject("Network.queryNavCountByID", dto);
			List ipqamList = (List)urmDao.queryForList("Network.queryIPqamInfoByNetwork", dto);
			for (Object object : ipqamList) {
				Dto ipqamDto = (Dto)object;
				Dto queryDto = new BaseDto();
				queryDto.put("ipqamInfoId", ipqamDto.getAsString("ipqaminfoid"));
				urmDao.delete("Qam.deleteResouceItemsByQamID", queryDto);
				urmDao.delete("Qam.deleteQamItems", queryDto);
				urmDao.delete("StreamResource.deleteStreamResourceByIPQAMID", queryDto);
			}
			//删除原表中的数据
			urmDao.delete("Network.deleteNetwork", dto);
			if(NavCount>0){
				SendMessageClient s = new SendMessageClient(Contents.NETWORK,Contents.DELETE,null,strNetRegionNum);  
				s.send();
			}
		}
		return outputDto;
	}

	public Dto updateNetworks(Dto inDto) {
		Dto retDto = new BaseDto();
//		int sum = inDto.getAsInteger("sum");
		boolean flag = inDto.getAsBoolean("flag");
		boolean changeflag = inDto.getAsBoolean("changeflag");
		if(!flag){
			retDto.put("success", FALSE);
		}else{
//			if(sum>0){
//				retDto.put("success", FALSE);
//			}else{
				String strNetRegionName = inDto.getAsString("strnetregionname");
				String iNavgationStreamNum = inDto.getAsString("inavgationstreamnum");
				String sdiNavgationStreamNum = inDto.getAsString("sdinavgationstreamnum");
				String iAdvertisementStreamNum = inDto.getAsString("iadvertisementstreamnum");
				String sdiAdvertisementStreamNum = inDto.getAsString("sdiadvertisementstreamnum");
				String strNetworkComment = inDto.getAsString("strnetworkcomment");
				Integer strNetRegionNum = inDto.getAsInteger("strnetregionnum");
			
				inDto.put("strNetRegionName", strNetRegionName);
				inDto.put("iNavgationStreamNum", iNavgationStreamNum);
				inDto.put("sdiNavgationStreamNum", sdiNavgationStreamNum);
				inDto.put("iAdvertisementStreamNum", iAdvertisementStreamNum);
				inDto.put("sdiAdvertisementStreamNum", sdiAdvertisementStreamNum);
				inDto.put("strNetworkComment", strNetworkComment);
				inDto.put("strNetRegionNum", strNetRegionNum);
				urmDao.update("Network.updateNetwork", inDto);	
				if(changeflag){
					SendMessageClient s = new SendMessageClient(Contents.NETWORK,Contents.UPDATE,null,String.valueOf(strNetRegionNum));  
					s.send();
				}
				retDto.put("success", TRUE);
//			}		
		}
	
		
		return retDto;
	}
	
	private Dto updateMDBForQamRes(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",3);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}

	
	//全量同步服务寻址到缓存
	public Dto saveCacheContor(Dto pDto) {
		String operater = pDto.getAsString("account");
		String password = pDto.getAsString("password");
		password = G4Utils.encryptBasedDes(password);
		String operpassword = pDto.getAsString("operpassword");
		Dto outDto = new BaseDto();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");//设置日期格式
		String oper_time = df.format(new Date(new java.util.Date().getTime()));
		if(password==operpassword||operpassword.equals(password)){
			outDto.put("success", TRUE);			
			log.info("操作员"+operater+"在" + oper_time + "进行了阈值全量同步操作!");
					
			pDto.put("status",1);
			pDto.put("id", IDHelper.getYunAppID());
			pDto.put("ipqam_event_id", IDHelper.getYunAppID());
			pDto.put("type", 10);
			Date create_date = new Date(new java.util.Date().getTime());
			pDto.put("create_date", create_date);
			pDto.put("oper_status", "98");
			
			urmDao.insert("Qam.dynamicLoadMDB",pDto);
			
			//添加操作记录
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id",busi_id);
			logDto.put("busi_code", "B11_44");
			logDto.put("oper_id", pDto.get("oper_id"));
			logDto.put("area_id", pDto.getAsString("dept_id"));
			urmDao.insert("Qam.saveOperLog",logDto);
			
			outDto.put("success", TRUE);
		}else{
			outDto.put("false", TRUE);
			log.error("操作员"+operater+"在"+oper_time+"进行了阈值全量同步操作，但是密码输入有误！属于非法操作");
		}
		
		return outDto;
	}

}
