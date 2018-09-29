package com.cmig.future.csportal.module.villager.family.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.family.dto.AssetsInfo;
import com.cmig.future.csportal.module.villager.family.mapper.AssetsInfoMapper;
import com.cmig.future.csportal.module.villager.family.service.IAssetsInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssetsInfoServiceImpl extends BaseServiceImpl<AssetsInfo> implements IAssetsInfoService{

	
	@Autowired
    private AssetsInfoMapper assetsInfoMapper;
    
	@Override
	public AssetsInfo queryAssets(String familyCode) {
		AssetsInfo info = assetsInfoMapper.selectByFamilyCode(familyCode);
		return info;
	}

	@Override
	public void updateByFamilyCode(String familyCode, String depositAmount, String depositBank, String isLoan) {

		assetsInfoMapper.updateByFamilyCode(familyCode,depositAmount,depositBank,isLoan);
		
	}

}