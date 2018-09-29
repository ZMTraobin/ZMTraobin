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
import com.cmig.future.csportal.module.villager.family.dto.FamilyInfo;
import com.cmig.future.csportal.module.villager.family.mapper.FamilyInfoMapper;
import com.cmig.future.csportal.module.villager.family.service.IFamilyInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FamilyInfoServiceImpl extends BaseServiceImpl<FamilyInfo> implements IFamilyInfoService{

	@Autowired
    private FamilyInfoMapper familyInfoMapper;
    
	@Override
	public FamilyInfo queryFamilyCode(Long mobile) {
		FamilyInfo info = familyInfoMapper.selectByFamilyCode(mobile);
		
		return info;
	};
	
	@Override
	public String queryFamily(String familyCode) {
		List<FamilyInfo> list = familyInfoMapper.queryFamily(familyCode);
		String per = "";
		for(FamilyInfo inf : list) {
			String mean = CodeUtil.getDictLabel(inf.getFamilyRelation(), Global.DICT_VILLAGER_FAMILY_RELATION, null, null);
			per = per + inf.getUserName() +"("+  mean +")，";
		}
		String res = per+"共"+list.size()+"口";
		return res;
	};
	
	@Override
	public List<JSONObject> convertList(String type) {
		List<JSONObject> objList = new ArrayList<JSONObject>();
		List<CodeValue> list = CodeUtil.getDictList(type, null);
		for(CodeValue val : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", val.getValue());
			jsonObject.put("text", val.getMeaning());
			objList.add(jsonObject);
		}
		return objList;
	};
}