package com.cmig.future.csportal.module.villager.family.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.villager.family.dto.LandInfo;

public interface LandInfoMapper extends Mapper<LandInfo>{
	
	LandInfo selectByFamilyCode(String encodeStr);

	void updateByFamilyCode(String familyCode, String landArea, String landPrice, String mainCrop);

}