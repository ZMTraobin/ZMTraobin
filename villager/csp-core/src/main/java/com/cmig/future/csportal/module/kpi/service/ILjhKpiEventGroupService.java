package com.cmig.future.csportal.module.kpi.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiEventGroup;

import java.util.Date;
import java.util.List;

public interface ILjhKpiEventGroupService extends IBaseService<LjhKpiEventGroup>, ProxySelf<ILjhKpiEventGroupService>{

	void synEventGroupKpi(String date) throws Exception;

	List<LjhKpiEventGroup> selectReport(int days, int topNum);

	List<LjhKpiEventGroup> selectReportForDate(Date startDate, Date endDate);
}