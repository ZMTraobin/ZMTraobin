package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 物管云员工密码修改失败重试定时任务
 */
public class MgtUserUpdatePasswordRetryJob extends AbstractJob {

    private Logger logger = LoggerFactory.getLogger(MgtUserUpdatePasswordRetryJob.class);

    @Autowired
    private IMgtUserService mgtUserService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
	    logger.info("物管云员工密码修改失败重试定时任务-开始......");
	    mgtUserService.synMgtUserPasswordRetryJob();
	    logger.info("物管云员工密码修改失败重试定时任务-结束......");
    }

}
