package com.cmig.future.csportal.module.villager.wealth.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantType;
import com.cmig.future.csportal.module.villager.wealth.mapper.PlantTypeMapper;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantTypeService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlantTypeServiceImpl extends BaseServiceImpl<PlantType> implements IPlantTypeService{

	@Autowired
    private PlantTypeMapper plantTypeMapper;
    
	@Override
	public List<PlantType> queryPlantType() {
		List<PlantType> list = plantTypeMapper.selectAll();
		return list;
	}

}