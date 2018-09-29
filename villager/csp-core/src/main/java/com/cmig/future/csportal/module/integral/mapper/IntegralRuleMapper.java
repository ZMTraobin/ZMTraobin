package com.cmig.future.csportal.module.integral.mapper;

import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.cmig.future.csportal.module.integral.dto.IntegralRule;
import com.hand.hap.mybatis.common.Mapper;

public interface IntegralRuleMapper extends Mapper<IntegralRule> {
    /**
     * 根据订单号和场景类型查询积分的详细信息
     * @param dto
     * @return
     */
    IntegralFlowing getIntegralByTypeAndOutTradeno(IntegralFlowing dto);

}