package com.cmig.future.csportal.module.kpi.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LjhKpiUserMapper extends Mapper<LjhKpiUser>{

	List<LjhKpiUser> selectReport(LjhKpiUser dto);

	List<LjhKpiUser> selectReportForDate(@Param("startDate")Date startDate, @Param("endDate")Date endDate);
}