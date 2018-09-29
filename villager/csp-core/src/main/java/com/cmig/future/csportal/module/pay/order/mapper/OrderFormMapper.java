package com.cmig.future.csportal.module.pay.order.mapper;

import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderFormMapper extends Mapper<OrderForm>{

    OrderForm findByOrderNumber(@Param(value = "orderNumber") String orderNumber);
    
    List<OrderForm> select(OrderForm order);

    OrderForm findBySourceOrderNumber(OrderForm orderform);

    List<MgtReceivableDetail> getByUserId(@Param(value = "appUserId") String appUserId);

    List<OrderForm> getAllByUserId(@Param(value = "appUserId") String userId);

    OrderForm selectOderDetail(Long id);

	/**
	 * 获取超时未支付的订单
	 * @return
	 */
	List<OrderForm> getCancelOrdersForJob();

    List<OrderForm> selectByCondition(OrderForm dto);

	List<OrderForm> getLifePaidList(@Param(value = "appUserId") String appUserId);
}