package com.cmig.future.csportal.module.villager.family.mapper;

import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface FamilyInfoMapper extends Mapper<FamilyInfo> {

	FamilyInfo selectByFamilyCode(Long mobile);

	List<FamilyInfo> findByIdcardAndName(FamilyInfo familyInfo);
	
	List<FamilyInfo> queryFamily(String familyCode);

}