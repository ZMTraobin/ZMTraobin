package com.cmig.future.csportal.module.kpi.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiDuration;

import java.util.List;

public interface ILjhKpiDurationService extends IBaseService<LjhKpiDuration>, ProxySelf<ILjhKpiDurationService>{

	void synDurationKpi(String date,String periodType) throws Exception;

	List<LjhKpiDuration> selectReportForDate(LjhKpiDuration ljhKpiDuration);
}