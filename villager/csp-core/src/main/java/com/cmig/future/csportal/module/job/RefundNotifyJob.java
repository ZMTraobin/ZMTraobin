package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefundNotifyMc;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RefundNotifyJob extends AbstractJob { 

    private Logger logger = LoggerFactory.getLogger(RefundNotifyJob.class);

    @Autowired
    private IOrderFormService service;

    @Override
    public void safeExecute(JobExecutionContext context) throws Exception {
        String times = context.getMergedJobDataMap().getString("times");
        OrderFormRefundNotifyMc orderFormRefundNotifyMc = new OrderFormRefundNotifyMc();
        orderFormRefundNotifyMc.setTimes(Long.valueOf(times));
        service.excuteOrderRefundNotifyJob(orderFormRefundNotifyMc);
    }

}
