package com.cmig.future.csportal.module.pay.order.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.pay.order.dto.OrderFormNotifyMc;

public interface IOrderFormNotifyMcService extends IBaseService<OrderFormNotifyMc>, ProxySelf<IOrderFormNotifyMcService>{

    List<OrderFormNotifyMc> selectOrderNotify(IRequest requestContext, OrderFormNotifyMc dto, int page, int pageSize);

}