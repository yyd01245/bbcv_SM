package org.eredlab.g4.urm.service;

import java.util.List;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

public interface DateSynchronizateService extends BaseService{

	int queryCagCountByIP(Dto capDto);

	void savecsgInfo(List csgInsertList);

	void savecagInfo(List cagInsertList);

	void saveckgInfo(List ckgInsertList);

	void updatecagInfo(List cagInsertList);

	void updatecsgInfo(List csgInsertList);

	void updateckgInfo(List ckgInsertList);

	void saveHost(String revStr);

	void saveApp(String revStr);

	void updateApp(String revStr);

	void saveConfigInfo(List list);

	void delConfigInfo(Dto dto);

}
