package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.properties.base.customer.service.IBpHouseMapService;
import com.cmig.future.csportal.module.sys.apilog.service.ISysLogService;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by dell on 2017/5/25.
 */
public class DeleteHouseMapDataJob extends AbstractJob {
    @Autowired
    private IBpHouseMapService service;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        service.excuteDeleteHouseMapDataJob();
    }

}
