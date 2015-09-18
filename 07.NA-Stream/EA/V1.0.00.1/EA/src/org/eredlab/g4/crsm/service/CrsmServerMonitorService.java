package org.eredlab.g4.crsm.service;

import java.util.List;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 云应用承载平台监控操作接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface CrsmServerMonitorService extends BaseService {


	public Dto savaConfigById(Dto inDto);

	public Dto saveCabinet(Dto inDto);

	public Dto savaBanding(Dto inDto);

	public Dto serverOnlineOperate(Dto inDto);

	public Dto serverOfflineOperate(Dto inDto);

	public String querySesionDetail(Dto inDto);

	public Dto queryServerSingle(Dto inDto);

	public Dto killSession(Dto inDto);

	public Dto deleteCabinet(Dto inDto);

	public void saveServerMore(List insertList);

	public void updateServerMore(List updateList);

	public Dto serverAuthOperate(Dto inDto);

	public Dto serverPerOfflineOperate(Dto inDto);

	public Dto serverDeleteCabinet(Dto inDto);

	public void updateServerStatus(Dto inDto);


}
