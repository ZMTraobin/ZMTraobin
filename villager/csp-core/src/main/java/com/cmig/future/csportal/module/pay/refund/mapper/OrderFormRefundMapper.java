
package com.cmig.future.csportal.module.pay.refund.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;

public interface OrderFormRefundMapper extends Mapper<OrderFormRefund>{

    OrderFormRefund findByRefundOrderNo(String refundOrderNo);

    List<OrderFormRefund> selectByOrder(OrderFormRefund dto);
}