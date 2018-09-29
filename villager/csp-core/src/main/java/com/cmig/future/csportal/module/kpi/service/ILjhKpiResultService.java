package com.cmig.future.csportal.module.kpi.service;

import com.cmig.future.csportal.module.kpi.dto.LjhKpiResult;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.Date;
import java.util.List;

public interface ILjhKpiResultService extends IBaseService<LjhKpiResult>, ProxySelf<ILjhKpiResultService>{


	List<LjhKpiResult> getReportForDate(LjhKpiResult dto);

	List<LjhKpiResult>  select(LjhKpiResult dto, int pageNum, int pageSize);

	void runResult(Date kpiDate) throws Exception;
}