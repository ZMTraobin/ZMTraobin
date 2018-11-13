package com.cmig.future.csportal.module.villager.wealth.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.villager.wealth.dto.WealthPlan;

public interface IWealthPlanService extends IBaseService<WealthPlan>, ProxySelf<IWealthPlanService>{

	List<WealthPlan> queryPlantList(String flag, int page, int pageSize, String keywords);

}