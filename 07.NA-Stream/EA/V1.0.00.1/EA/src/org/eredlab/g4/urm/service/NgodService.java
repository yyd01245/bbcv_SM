package org.eredlab.g4.urm.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

public interface NgodService extends BaseService {

	Dto saveNgodInfo(Dto inDto);

	Dto updateNgodInfo(Dto inDto);

	Dto deleteNgodInfo(Dto inDto);

	Dto saveNgodRfInfo(Dto inDto);

	Dto updateNgodRfInfo(Dto inDto);

	Dto deleteNgodRfInfo(Dto inDto);

}
