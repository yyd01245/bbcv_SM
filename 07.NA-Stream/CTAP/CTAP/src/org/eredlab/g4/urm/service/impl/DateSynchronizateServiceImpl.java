package org.eredlab.g4.urm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.urm.service.DateSynchronizateService;

public class DateSynchronizateServiceImpl extends BaseServiceImpl implements DateSynchronizateService {
	Log logger = LogFactory.getLog(DateSynchronizateServiceImpl.class);
	
	public int queryCagCountByIP(Dto capDto) {

		return 0;
	}

	public void savecsgInfo(List csgInsertList) {
		try {
			urmDao.batchInsert("DateSyn.saveCsgInfo", csgInsertList);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}	
	}

	public void savecagInfo(List cagInsertList) {
		try {
			urmDao.batchInsert("DateSyn.saveCagInfo", cagInsertList);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}	
		
	}

	public void saveckgInfo(List ckgInsertList) {
		try {
			urmDao.batchInsert("DateSyn.saveCkgInfo", ckgInsertList);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}	
		
	}

	public void updatecagInfo(List cagInsertList) {
		if(cagInsertList.size()>0){
			for (Object object : cagInsertList) {
				Dto dto = (Dto)object;
				urmDao.update("DateSyn.updateCagInfo", dto);
			}
		}
		
	}

	public void updatecsgInfo(List csgInsertList) {
		if(csgInsertList.size()>0){
			for (Object object : csgInsertList) {
				Dto dto = (Dto)object;
				urmDao.update("DateSyn.updateCsgInfo", dto);
			}
		}
		
	}

	public void updateckgInfo(List ckgInsertList) {
		if(ckgInsertList.size()>0){
			for (Object object : ckgInsertList) {
				Dto dto = (Dto)object;
				urmDao.update("DateSyn.updateCkgInfo", dto);
			}
		}
		
	}

	public void saveApp(String revStr) {
		try {
			if(!(revStr==null||"".equals(revStr))){
				revStr=revStr.replace("XXEE", "");
				Dto retDto = JsonHelper.parseSingleJson2Dto(revStr);
				List cagInsertList = new ArrayList();
				List cagUpdateList = new ArrayList();	//cag修改列表
				List csgInsertList = new ArrayList();	//csg新增列表
				List csgUpdateList = new ArrayList();	//csg修改列表
				List ckgInsertList = new ArrayList();	//ckg新增列表
				List ckgUpdateList = new ArrayList();	//ckg修改列表
				
				List csgList = retDto.getAsList("csgpool");
				List ckgList = retDto.getAsList("ckgpool");
				String cap_id = (String)urmDao.queryForObject("DateSyn.queryCagIDbyIP", retDto);
				if(cap_id==null||"".equals(cap_id)){
					//数据库中没有记录，新增
					Integer id = (Integer)urmDao.queryForObject("DateSyn.queryMaxIndexForCag", retDto);
					Integer cag_id = id==null?1:(id+1);
					retDto.put("cag_id", cag_id);
					cagInsertList.add(retDto);
					for (Object object2 : csgList) {
						Dto csgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object2));
						csgDto.put("cag_id", cag_id);
						csgInsertList.add(csgDto);
					}
					//统计ckg列表
					for (Object object3 : ckgList) {
						Dto ckgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object3));
						ckgDto.put("cag_id", cag_id);
						ckgInsertList.add(ckgDto);
					}
					
				}else{
					retDto.put("cag_id", cap_id);
					cagUpdateList.add(retDto);
					//统计csg列表
					for (Object object2 : csgList) {
						Dto csgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object2));
						csgDto.put("cag_id", cap_id);
						String csg_id = (String)urmDao.queryForObject("DateSyn.queryCsgIDbyKey", csgDto);
						if(csg_id==null||"".equals(csg_id)){
							csgInsertList.add(csgDto);
						}else{
							csgDto.put("csg_id", csg_id);
							csgUpdateList.add(csgDto);
						}
					}
					//统计ckg列表
					for (Object object3 : ckgList) {
						Dto ckgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object3));
						ckgDto.put("cag_id", cap_id);
						String ckg_id = (String)urmDao.queryForObject("DateSyn.queryCkgIDbyKey", ckgDto);
						if(ckg_id==null||"".equals(ckg_id)){
							ckgInsertList.add(ckgDto);
						}else{
							ckgDto.put("ckg_id", ckg_id);
							ckgUpdateList.add(ckgDto);
						}
					}
				}
				savecagInfo(cagInsertList);
				Thread.sleep(100);
				savecsgInfo(csgInsertList);
				Thread.sleep(100);
				saveckgInfo(ckgInsertList);
				Thread.sleep(100);
				updatecagInfo(cagUpdateList);
				Thread.sleep(100);
				updatecsgInfo(csgUpdateList);
				Thread.sleep(100);
				updateckgInfo(ckgUpdateList);
			}
		} catch (Exception e) {
		}
		
	}

	public void saveHost(String revStr) {
		try {
			if(!(revStr==null||"".equals(revStr))){
				revStr=revStr.replace("XXEE", "");
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				urmDao.update("DateSyn.updateCAGtable", dto);
				Thread.sleep(100);
				urmDao.update("DateSyn.updateCSGtable", dto);
				Thread.sleep(100);
				urmDao.update("DateSyn.updateCKGtable", dto);
			}
		} catch (Exception e) {
		}
	}

	public void updateApp(String revStr) {
		try {
			if(!(revStr==null||"".equals(revStr))){
				revStr=revStr.replace("XXEE", "");
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				String type = dto.getAsString("type");
				CommandSupport.TYPE cc = CommandSupport.TYPE
						.valueOf(type);
				switch (cc) {
				case cag:
					urmDao.update("DateSyn.updateCagInfoByIpPort", dto);
					break;
				case csg:
					urmDao.update("DateSyn.updateCsgInfoByIpPort", dto);
					break;
				case ckg:
					urmDao.update("DateSyn.updateCkgInfoByIpPort", dto);
					break;
				
				}
			}
		} catch (Exception e) {
		}
	}

	public void saveConfigInfo(List list) {
		try {
			urmDao.delete("DateSyn.delConfigInfo");
			urmDao.batchInsert("DateSyn.saveConfigInfo", list);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}	
		
	}

	public void delConfigInfo(Dto dto) {
		try {
			urmDao.delete("DateSyn.delConfigInfo",dto);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}	
		
	}

}
