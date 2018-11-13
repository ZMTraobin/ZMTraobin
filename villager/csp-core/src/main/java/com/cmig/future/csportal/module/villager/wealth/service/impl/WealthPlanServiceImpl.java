package com.cmig.future.csportal.module.villager.wealth.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmig.future.csportal.module.villager.wealth.dto.WealthPlan;
import com.cmig.future.csportal.module.villager.wealth.service.IWealthPlanService;
import com.github.pagehelper.PageHelper;
import com.cmig.future.csportal.module.villager.wealth.mapper.WealthPlanMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WealthPlanServiceImpl extends BaseServiceImpl<WealthPlan> implements IWealthPlanService{

	@Autowired
    private WealthPlanMapper WealthPlanMapper;
    
	@Override
	public List<WealthPlan> queryPlantList(String flag, int page, int pageSize, String keywords) {
		PageHelper.startPage(page,pageSize);
		List<WealthPlan> list= WealthPlanMapper.queryPlantList(flag,keywords);
		return list;
	}

}