/*
 * #{copyright}#
 */

package com.cmig.future.csportal.module.sys.job.service;

import java.util.List;

import com.cmig.future.csportal.module.sys.job.dto.JobDetailDto;
import com.hand.hap.core.IRequest;
import com.hand.hap.job.dto.JobInfoDetailDto;

/**
 * @author shengyang.zhou@hand-china.com
 */
public interface ISysQuartzService {
    /**
     * 查询job列表.
     * 
     * @param request
     *            session信息
     * 
     * @param example
     *            查询参数
     * @param page
     *            页码
     * @param pagesize
     *            每页数量
     * @return job结果列表
     */
    List<JobInfoDetailDto> getJobInfoDetails(IRequest request, JobDetailDto example, int page, int pagesize);

}
