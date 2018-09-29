package com.cmig.future.csportal.module.villager.integral.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;
import java.util.Map;

import com.cmig.future.csportal.module.villager.integral.dto.IntegralDetail;

public interface IIntegralDetailService extends IBaseService<IntegralDetail>, ProxySelf<IIntegralDetailService>{

	List<IntegralDetail> queryDetail(String appUserId,String month);
	Map insertDetail(String code,String uid);
	Map convertToScores(String code,String coin,String uid);
}