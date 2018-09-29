package com.cmig.future.csportal.module.kpi.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiUser;

import java.util.Date;
import java.util.List;

public interface ILjhKpiUserService extends IBaseService<LjhKpiUser>, ProxySelf<ILjhKpiUserService>{


	void synUserKpi(String date) throws Exception;


	List<LjhKpiUser> selectReport(LjhKpiUser dto);

	List<LjhKpiUser> selectReportForDate(Date startDate, Date endDate);
}