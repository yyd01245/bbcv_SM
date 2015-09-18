package org.eredlab.g4.cabs.service.impl;


import java.util.Calendar;
import java.util.Date;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.cabs.service.UpdateStrategyService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.mortbay.log.Log;

public class UpdateStrategyServiceImpl extends BaseServiceImpl implements UpdateStrategyService{

	public Dto insertStrategyItem(Dto inDto) {
		Dto outDto = new BaseDto();

		crsmDao.delete("UpdateStrategy.deleteUpdateStrategy", inDto);
			Integer id = (Integer)crsmDao.queryForObject("UpdateStrategy.queryRelMaxIndex");//获取主键
			if(id==null){
				id=0;
			}
			Calendar cal=Calendar.getInstance(); 
			Date createdate=cal.getTime(); 
			inDto.put("gv_id", id+1);
			inDto.put("create_time", createdate);
			crsmDao.insert("UpdateStrategy.saveUpdateStrategyItem", inDto);

			outDto.put("success", new Boolean(true));
//		}
		return outDto;
	}

	public Dto deleteStrategyItems(Dto inDto) {
		// TODO Auto-generated method stub
		Dto outDto = new BaseDto();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			Dto dto = new BaseDto();
			dto.put("gv_id", arrChecked[i]);
			Log.info("gv_id=============================>"+arrChecked[i]);

			crsmDao.delete("UpdateStrategy.deleteUpdateStrategyItems", dto);
		}
		outDto.put("success", new Boolean(true));
		return outDto;
	}

}
