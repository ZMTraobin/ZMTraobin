package com.cmig.future.csportal.module.villager.family.service.impl;

import com.hand.hap.system.dto.CodeValue;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.cmig.future.csportal.module.villager.family.dto.LandInfo;
import com.cmig.future.csportal.module.villager.family.mapper.LandInfoMapper;
import com.cmig.future.csportal.module.villager.family.service.ILandInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LandInfoServiceImpl extends BaseServiceImpl<LandInfo> implements ILandInfoService{

	@Autowired
    private LandInfoMapper landInfoMapper;
    
	@Override
	public LandInfo queryLand(String familyCode) {
		LandInfo info = landInfoMapper.selectByFamilyCode(familyCode);
		
		return info;
	}

	@Override
	public void updateByFamilyCode(String familyCode, String landArea, String landPrice, String mainCrop) {
		landInfoMapper.updateByFamilyCode(familyCode,landArea,landPrice,mainCrop);
	}

}