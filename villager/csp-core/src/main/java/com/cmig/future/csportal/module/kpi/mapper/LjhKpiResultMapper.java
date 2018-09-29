package com.cmig.future.csportal.module.kpi.mapper;

import com.cmig.future.csportal.module.kpi.dto.LjhKpiResult;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface LjhKpiResultMapper extends Mapper<LjhKpiResult>{

	List<LjhKpiResult> getReportForDate(LjhKpiResult dto);

}