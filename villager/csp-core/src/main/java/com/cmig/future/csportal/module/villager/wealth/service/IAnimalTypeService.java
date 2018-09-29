package com.cmig.future.csportal.module.villager.wealth.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.villager.wealth.dto.AnimalType;

public interface IAnimalTypeService extends IBaseService<AnimalType>, ProxySelf<IAnimalTypeService>{

	List<AnimalType> queryAnimalType();

}