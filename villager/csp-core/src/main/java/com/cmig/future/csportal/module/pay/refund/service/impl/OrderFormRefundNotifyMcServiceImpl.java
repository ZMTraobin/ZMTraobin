package com.cmig.future.csportal.module.pay.refund.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefundNotifyMc;
import com.cmig.future.csportal.module.pay.refund.service.IOrderFormRefundNotifyMcService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderFormRefundNotifyMcServiceImpl extends BaseServiceImpl<OrderFormRefundNotifyMc> implements IOrderFormRefundNotifyMcService{

}