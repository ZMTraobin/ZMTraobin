package com.cmig.future.csportal.module.pay.order.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.pay.order.dto.OrderFormNotifyMc;
import com.cmig.future.csportal.module.pay.order.mapper.OrderFormNotifyMcMapper;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormNotifyMcService;
import com.github.pagehelper.PageHelper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderFormNotifyMcServiceImpl extends BaseServiceImpl<OrderFormNotifyMc>
        implements IOrderFormNotifyMcService {

    @Autowired
    private OrderFormNotifyMcMapper orderFormNotifyMcMapper;

    @Override
    public List<OrderFormNotifyMc> selectOrderNotify(IRequest requestContext, OrderFormNotifyMc dto, int page,
            int pageSize) {
        PageHelper.startPage(page, pageSize);
        return orderFormNotifyMcMapper.findByOrderNotify(dto);
    }

}