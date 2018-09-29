package com.cmig.future.csportal.module.villager.family.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.villager.family.dto.AssetsInfo;

public interface AssetsInfoMapper extends Mapper<AssetsInfo>{
	
	AssetsInfo selectByFamilyCode(String encodeStr);

	void updateByFamilyCode(String familyCode, String depositAmount, String depositBank, String isLoan);

}