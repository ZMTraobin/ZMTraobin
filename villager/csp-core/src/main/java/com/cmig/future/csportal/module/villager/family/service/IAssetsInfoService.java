package com.cmig.future.csportal.module.villager.family.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.villager.family.dto.AssetsInfo;

public interface IAssetsInfoService extends IBaseService<AssetsInfo>, ProxySelf<IAssetsInfoService>{

	AssetsInfo queryAssets(String familyCode);

	void updateByFamilyCode(String familyCode, String depositAmount, String depositBank, String isLoan);

}