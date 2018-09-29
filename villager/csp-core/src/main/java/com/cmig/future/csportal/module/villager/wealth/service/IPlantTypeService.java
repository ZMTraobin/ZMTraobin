package com.cmig.future.csportal.module.villager.wealth.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.villager.wealth.dto.PlantType;

public interface IPlantTypeService extends IBaseService<PlantType>, ProxySelf<IPlantTypeService>{

	List<PlantType> queryPlantType();

}