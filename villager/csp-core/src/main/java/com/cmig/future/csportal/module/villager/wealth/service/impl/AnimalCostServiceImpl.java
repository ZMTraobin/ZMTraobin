package com.cmig.future.csportal.module.villager.wealth.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalCost;
import com.cmig.future.csportal.module.villager.wealth.mapper.AnimalCostMapper;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalCostService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnimalCostServiceImpl extends BaseServiceImpl<AnimalCost> implements IAnimalCostService{

	@Autowired
    private AnimalCostMapper animalCostMapper;
    
	@Override
	public List<AnimalCost> queryAnimalCost(AnimalCost animalCost) {
		List<AnimalCost> list = animalCostMapper.select(animalCost);
		return list;
	}

}