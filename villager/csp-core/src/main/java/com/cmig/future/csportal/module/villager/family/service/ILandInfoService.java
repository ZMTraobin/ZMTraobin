package com.cmig.future.csportal.module.villager.family.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.cmig.future.csportal.module.villager.family.dto.LandInfo;

public interface ILandInfoService extends IBaseService<LandInfo>, ProxySelf<ILandInfoService>{

	LandInfo queryLand(String familyCode);

	void updateByFamilyCode(String familyCode, String landArea, String landPrice, String mainCrop);
	
}