package com.cmig.future.csportal.module.villager.integral.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.Date;
import java.util.List;

import com.cmig.future.csportal.module.villager.integral.dto.IntegralDetail;

public interface IntegralDetailMapper extends Mapper<IntegralDetail>{

	List<IntegralDetail> queryDetail(String appUserId,String month);
	IntegralDetail queryLastDetail(String userId);
	int selectByCode(String code,String date);
	int selectByCycle(String code,Date start,Date end);
	
}