
package com.cmig.future.csportal.module.pay.refund.service.impl;

import com.cmig.future.csportal.module.pay.conf.PingppConfig;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefundNotifyMc;
import com.cmig.future.csportal.module.pay.refund.mapper.OrderFormRefundMapper;
import com.cmig.future.csportal.module.pay.refund.mapper.OrderFormRefundNotifyMcMapper;
import com.cmig.future.csportal.module.pay.refund.service.IOrderFormRefundService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.pingplusplus.model.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderFormRefundServiceImpl extends BaseServiceImpl<OrderFormRefund> implements IOrderFormRefundService{

    @Autowired
    private OrderFormRefundMapper orderFormRefundMapper;

    @Autowired
    private OrderFormRefundNotifyMcMapper orderFormRefundNotifyMcMapper;

    @Override
    public Refund createRefund(OrderForm orderForm, Map<String, Object> params,OrderFormRefund orderFormRefund)throws Exception {
        PingppConfig.initPingxxAppid(orderForm.getAppId());
        Refund refund = Refund.create(orderForm.getChargeId(), params);
        if(refund.getSucceed()) {
            orderFormRefund.setRefundId(refund.getId());
            orderFormRefund.setRefundOrderNo(refund.getOrderNo());
            orderFormRefund.setAmount(Long.valueOf(refund.getAmount()));
            orderFormRefund.setStatus(refund.getStatus());
            orderFormRefund.setTimeSucceed(DateUtils.unixTimeToDate(refund.getTimeSucceed()));
            orderFormRefund.setDescription(refund.getDescription());
            orderFormRefund.setFailureCode(refund.getFailureCode());
            orderFormRefund.setFailureMsg(refund.getFailureMsg());
            orderFormRefund.setTransactionNo(refund.getTransactionNo());
            orderFormRefund.setOrderId(orderForm.getId());
            orderFormRefund.setOrderNumber(orderForm.getOrderNumber());
            orderFormRefund.setChargeId(refund.getCharge());
            orderFormRefundMapper.insertSelective(orderFormRefund);

	        OrderFormRefundNotifyMc orderFormRefundNotifyMc = new OrderFormRefundNotifyMc();
	        orderFormRefundNotifyMc.setRefundOrderId(orderFormRefund.getId());
	        orderFormRefundNotifyMc.setTimeNextNotify(new Date());// 第一次默认立即通知
	        orderFormRefundNotifyMcMapper.insertSelective(orderFormRefundNotifyMc);
        }else{
            orderFormRefund.setRefundId(refund.getId());
            orderFormRefund.setRefundOrderNo(refund.getOrderNo());
            orderFormRefund.setAmount(Long.valueOf(refund.getAmount()));
            orderFormRefund.setStatus(refund.getStatus());
            orderFormRefund.setDescription(refund.getDescription());
            orderFormRefund.setFailureCode(refund.getFailureCode());
            orderFormRefund.setFailureMsg(refund.getFailureMsg());
            orderFormRefund.setTransactionNo(refund.getTransactionNo());
            orderFormRefund.setOrderId(orderForm.getId());
            orderFormRefund.setOrderNumber(orderForm.getOrderNumber());
            orderFormRefund.setChargeId(refund.getCharge());
            orderFormRefundMapper.insertSelective(orderFormRefund);
        }
        return refund;
    }

    @Override
    public OrderFormRefund findByRefundOrderNo(String refundOrderNo) {
        return orderFormRefundMapper.findByRefundOrderNo(refundOrderNo);
    }

    @Override
    public List<OrderFormRefund> selectByOrder(IRequest requestContext, OrderFormRefund dto, int page, int pageSize) {
        return orderFormRefundMapper.selectByOrder(dto);
    }
}