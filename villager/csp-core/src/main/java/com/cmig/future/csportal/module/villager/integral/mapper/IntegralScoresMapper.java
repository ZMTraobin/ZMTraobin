package com.cmig.future.csportal.module.villager.integral.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralScores;

public interface IntegralScoresMapper extends Mapper<IntegralScores>{
	
	IntegralScores selectByCode(String scoresCode);

}