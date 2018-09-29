package com.cmig.future.csportal.module.kpi.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiDuration;

import java.util.List;

public interface LjhKpiDurationMapper extends Mapper<LjhKpiDuration>{

	List<LjhKpiDuration> selectReportForDate(LjhKpiDuration ljhKpiDuration);
}