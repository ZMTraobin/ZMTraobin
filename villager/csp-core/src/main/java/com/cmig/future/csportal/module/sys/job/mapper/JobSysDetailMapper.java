/*
 * #{copyright}#
 */

package com.cmig.future.csportal.module.sys.job.mapper;

import java.util.List;

import com.cmig.future.csportal.module.sys.job.dto.JobDetailDto;
import com.hand.hap.job.dto.JobInfoDetailDto;

/**
 *
 * @author shengyang.zhou@hand-china.com
 */
public interface JobSysDetailMapper {
    List<JobInfoDetailDto> selectJobInfoDetails(JobDetailDto example);
}