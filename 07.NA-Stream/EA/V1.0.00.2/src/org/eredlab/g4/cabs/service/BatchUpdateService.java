package org.eredlab.g4.cabs.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

public interface BatchUpdateService extends BaseService {
	public Dto batchUpdateItems(Dto pDto);
	public Dto batchOfflineItems(Dto pDto);
	public Dto batchRollBlockItems(Dto pDto);
	public Dto batchStartItems(Dto pDto);
	public Dto batchStopItems(Dto pDto);
	public Dto updateLocation(Dto pDto);
}
