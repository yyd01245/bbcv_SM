package org.eredlab.g4.urm.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

public interface QamDyService extends BaseService {

	Dto saveQAMDevice(Dto inDto);

	Dto updateQAMDevice(Dto inDto);

	Dto deleteDyQam(Dto inDto);

	Dto updateRfStatus(Dto inDto);

	Dto saveMidRf(Dto inDto);

	Dto saveNormalData(Dto inDto);

	Dto saveResData(Dto out);

	Dto batchPrompt(Dto inDto);

	Dto saveAllToNormal(Dto inDto);

}
