package com.cmig.future.csportal.module.pay.order.mapper;

import com.cmig.future.csportal.module.pay.order.dto.OrderFormNotifyMc;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface OrderFormNotifyMcMapper extends Mapper<OrderFormNotifyMc>{

    OrderFormNotifyMc findByOrderId(OrderFormNotifyMc orderFormNotifyMc);
    
    List<OrderFormNotifyMc> findByOrderNotify(OrderFormNotifyMc orderFormNotifyMc);

    List<OrderFormNotifyMc> getNotifyOrdersForJob(OrderFormNotifyMc orderFormNotifyMc);

}