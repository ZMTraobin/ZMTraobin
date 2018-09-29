package com.cmig.future.csportal.module.villager.family.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;

public interface HousingInfoMapper extends Mapper<HousingInfo>{
	
	HousingInfo selectByFamilyCode(String encodeStr);

	void updateByFamilyCode(String familyCode, Long buildYear, String buildprice, Long floors, String homestead,
			String houseArea);

}