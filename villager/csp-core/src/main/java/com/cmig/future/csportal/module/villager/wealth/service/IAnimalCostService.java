package com.cmig.future.csportal.module.villager.wealth.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.villager.wealth.dto.AnimalCost;

public interface IAnimalCostService extends IBaseService<AnimalCost>, ProxySelf<IAnimalCostService>{

	List<AnimalCost> queryAnimalCost(AnimalCost animalCost);

}