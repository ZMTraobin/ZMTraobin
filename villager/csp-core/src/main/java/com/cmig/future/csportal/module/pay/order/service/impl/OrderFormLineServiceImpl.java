package com.cmig.future.csportal.module.pay.order.service.impl;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormLine;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormLineMapper;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormLineService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderFormLineServiceImpl extends BaseServiceImpl<OrderFormLine> implements IOrderFormLineService{

	@Autowired
	private OrderFormLineMapper orderFormLineMapper;

	@Override
	@Transactional(readOnly = false)
	public void save(OrderFormLine orderFormLine) {
		if(!StringUtils.isEmpty(orderFormLine.getLineId())){
			orderFormLineMapper.updateByPrimaryKeySelective(orderFormLine);
		}else{
			super.mapper.insertSelective(orderFormLine);
		}
	}
}