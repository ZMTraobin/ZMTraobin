package com.cmig.future.csportal.module.pay.order.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderFormLineMapper extends Mapper<OrderFormLine>{

	List<OrderFormLine> findByOrderId(@Param(value = "orderId") Long orderId);
}