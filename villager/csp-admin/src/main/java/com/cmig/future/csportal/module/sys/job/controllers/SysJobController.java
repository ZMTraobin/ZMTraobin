/*
 * #{copyright}#
 */

package com.cmig.future.csportal.module.sys.job.controllers;

import javax.servlet.http.HttpServletRequest;

import com.cmig.future.csportal.module.sys.job.dto.JobDetailDto;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmig.future.csportal.module.sys.job.service.ISysQuartzService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

/**
 * @author shengyang.zhou@hand-china.com
 */
@Controller
@RequestMapping("/job")
public class SysJobController extends BaseController {

    @Autowired
    private ISysQuartzService sysQuartzService;
   
    /**
     * 
     * 查询job列表.
     * 
     * @param example
     *            查询参数
     * @param page
     *            页码
     * @param pagesize
     *            每页数量
     * @param request
     *            HttpServletRequest
     * @return job结果列表
     * @throws SchedulerException
     *             Base class for exceptions thrown by the Quartz Scheduler.
     */
    @RequestMapping("/queryByCondition")
    public ResponseData queryJobs(@ModelAttribute JobDetailDto example,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize, HttpServletRequest request)
                    throws SchedulerException {
        return qj(example, page, pagesize, request);
    }

    /**
     *
     * 查询job列表.
     *
     * @param example
     *            查询参数
     * @param page
     *            页码
     * @param pagesize
     *            每页数量
     * @param request
     *            HttpServletRequest
     * @return job结果列表
     * @throws SchedulerException
     *             Base class for exceptions thrown by the Quartz Scheduler.
     */
    @RequestMapping("/queryInfoCondition")
    public ResponseData query(@RequestBody JobDetailDto example, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize, HttpServletRequest request)
            throws SchedulerException {
        return qj(example, page, pagesize, request);
    }
    
    private ResponseData qj(JobDetailDto example, int page, int pagesize, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(sysQuartzService.getJobInfoDetails(requestCtx, example, page, pagesize));
    }
}
