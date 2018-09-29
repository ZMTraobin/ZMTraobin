package com.cmig.future.csportal.module.kpi.mapper;

import com.cmig.future.csportal.module.kpi.dto.LjhKpiEventGroup;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LjhKpiEventGroupMapper extends Mapper<LjhKpiEventGroup>{

	List<LjhKpiEventGroup> selectReport(@Param("startDate")Date startDate, @Param("endDate")Date endDate,@Param("topNum") int topNum);

	List<LjhKpiEventGroup> selectReportForDate(@Param("startDate")Date startDate, @Param("endDate")Date endDate);

	List<LjhKpiEventGroup> getHaveDataDates(@Param("days")int days);
}