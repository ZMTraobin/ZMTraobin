package com.cmig.future.csportal.module.villager.family.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;

public interface IHousingInfoService extends IBaseService<HousingInfo>, ProxySelf<IHousingInfoService>{

	HousingInfo queryHouse(String familyCode);

	void updateByFamilyCode(String familyCode, Long buildYear, String buildprice, Long floors, String homestead,
			String houseArea);

}