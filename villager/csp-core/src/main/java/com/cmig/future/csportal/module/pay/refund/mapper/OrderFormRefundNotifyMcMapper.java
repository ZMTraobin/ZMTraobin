package com.cmig.future.csportal.module.pay.refund.mapper;

import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefundNotifyMc;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface OrderFormRefundNotifyMcMapper extends Mapper<OrderFormRefundNotifyMc>{

    OrderFormRefundNotifyMc findByRefundOrderId(OrderFormRefundNotifyMc orderFormRefundNotifyMc);

    List<OrderFormRefundNotifyMc> findByOrderNotify(OrderFormRefundNotifyMc orderFormRefundNotifyMc);

    List<OrderFormRefundNotifyMc> getNotifyOrdersForJob(OrderFormRefundNotifyMc orderFormRefundNotifyMc);
}