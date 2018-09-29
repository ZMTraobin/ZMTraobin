package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.pay.order.dto.OrderFormNotifyMc;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderFormNotifyJob extends AbstractJob { 

    private Logger logger = LoggerFactory.getLogger(OrderFormNotifyJob.class);

    @Autowired
    private IOrderFormService service;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        String times = context.getMergedJobDataMap().getString("times");
        OrderFormNotifyMc orderFormNotifyMc = new OrderFormNotifyMc();
        orderFormNotifyMc.setTimes(Long.valueOf(times));
        service.excuteOrderNotifyJob(orderFormNotifyMc);
    }

}
