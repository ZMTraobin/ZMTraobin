package com.cmig.future.csportal.module.villager.wealth.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantCost;
import com.cmig.future.csportal.module.villager.wealth.mapper.PlantCostMapper;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantCostService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlantCostServiceImpl extends BaseServiceImpl<PlantCost> implements IPlantCostService{

	@Autowired
    private PlantCostMapper plantCostMapper;
    
	@Override
	public List<PlantCost> queryPlantCost(PlantCost plantCost) {
		List<PlantCost> list = plantCostMapper.select(plantCost);
		return list;
	}

}