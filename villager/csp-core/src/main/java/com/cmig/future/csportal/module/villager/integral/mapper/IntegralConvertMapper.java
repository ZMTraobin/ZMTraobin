package com.cmig.future.csportal.module.villager.integral.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralConvert;

public interface IntegralConvertMapper extends Mapper<IntegralConvert>{

	IntegralConvert selectByCode(String scoresCode);
}