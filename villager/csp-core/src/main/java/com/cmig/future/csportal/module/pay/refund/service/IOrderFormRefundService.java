
package com.cmig.future.csportal.module.pay.refund.service;

import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;
import com.pingplusplus.model.Refund;

import java.util.List;
import java.util.Map;

public interface IOrderFormRefundService extends IBaseService<OrderFormRefund>, ProxySelf<IOrderFormRefundService>{

    Refund createRefund(OrderForm orderForm, Map<String, Object> params, OrderFormRefund orderFormRefund)throws Exception;

    OrderFormRefund findByRefundOrderNo(String refundOrderNo);

    List<OrderFormRefund> selectByOrder(IRequest requestContext, OrderFormRefund dto, int page, int pageSize);
}