package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 订单超时未支付取消定时任务
 */
public class OrderFormCancelJob extends AbstractJob {

    private Logger logger = LoggerFactory.getLogger(OrderFormCancelJob.class);

    @Autowired
    private IOrderFormService orderFormService;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
	    logger.info("订单超时未支付取消定时任务-开始......");
        String times = context.getMergedJobDataMap().getString("times");
	    orderFormService.excuteOrderCancelJob();
	    logger.info("订单超时未支付取消定时任务-结束......");
    }

}
