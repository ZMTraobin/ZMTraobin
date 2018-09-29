package com.cmig.future.csportal.module.villager.family.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;
import com.cmig.future.csportal.module.villager.family.mapper.HousingInfoMapper;
import com.cmig.future.csportal.module.villager.family.service.IHousingInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HousingInfoServiceImpl extends BaseServiceImpl<HousingInfo> implements IHousingInfoService{

	@Autowired
    private HousingInfoMapper housingInfoMapper;
    
	@Override
	public HousingInfo queryHouse(String familyCode) {
		HousingInfo info = housingInfoMapper.selectByFamilyCode(familyCode);
		return info;
	}

	@Override
	public void updateByFamilyCode(String familyCode, Long buildYear, String buildprice, Long floors, String homestead,
			String houseArea) {
		housingInfoMapper.updateByFamilyCode(familyCode,buildYear,buildprice,floors,homestead,houseArea);
		
	}

}