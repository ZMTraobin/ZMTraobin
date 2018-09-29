package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.sys.apilog.service.ISysLogService;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by dell on 2017/5/25.
 */
public class DeleteLogDataJob extends AbstractJob {
    @Autowired
    ISysLogService sysLogService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        sysLogService.excuteDeleteLogDataJob();
    }

}
