package com.cmig.future.csportal.module.villager.wealth.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmig.future.csportal.module.villager.wealth.dto.WealthPlan;

public interface WealthPlanMapper extends Mapper<WealthPlan>{

	List<WealthPlan> queryPlantList(@Param("flag") String flag, @Param("keywords") String keywords);

}