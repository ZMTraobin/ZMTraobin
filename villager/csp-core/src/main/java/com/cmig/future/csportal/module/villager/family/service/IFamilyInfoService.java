package com.cmig.future.csportal.module.villager.family.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import net.sf.json.JSONObject;

import java.util.List;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;

public interface IFamilyInfoService extends IBaseService<FamilyInfo>, ProxySelf<IFamilyInfoService>{

	FamilyInfo queryFamilyCode(Long mobile);
	String queryFamily(String familyCode);
	List<JSONObject> convertList(String type);
}