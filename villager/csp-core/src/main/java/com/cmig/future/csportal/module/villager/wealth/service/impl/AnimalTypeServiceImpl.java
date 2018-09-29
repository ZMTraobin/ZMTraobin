package com.cmig.future.csportal.module.villager.wealth.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalType;
import com.cmig.future.csportal.module.villager.wealth.mapper.AnimalTypeMapper;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalTypeService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnimalTypeServiceImpl extends BaseServiceImpl<AnimalType> implements IAnimalTypeService{

	@Autowired
    private AnimalTypeMapper animalTypeMapper;
    
	@Override
	public List<AnimalType> queryAnimalType() {
		List<AnimalType> list = animalTypeMapper.selectAll();
		return list;
	}

}