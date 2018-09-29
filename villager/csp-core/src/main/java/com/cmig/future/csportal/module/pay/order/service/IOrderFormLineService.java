package com.cmig.future.csportal.module.pay.order.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormLine;

public interface IOrderFormLineService extends IBaseService<OrderFormLine>, ProxySelf<IOrderFormLineService>{

	void save(OrderFormLine orderFormLine);
}