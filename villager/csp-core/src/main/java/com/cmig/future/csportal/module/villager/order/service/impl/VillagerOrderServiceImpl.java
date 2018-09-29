package com.cmig.future.csportal.module.villager.order.service.impl;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.cmig.future.csportal.module.villager.good.mapper.VillagerGoodMapper;
import com.cmig.future.csportal.module.villager.order.dto.VillagerOrder;
import com.cmig.future.csportal.module.villager.order.mapper.VillagerOrderMapper;
import com.cmig.future.csportal.module.villager.order.service.IVillagerOrderService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VillagerOrderServiceImpl extends BaseServiceImpl<VillagerOrder> implements IVillagerOrderService{

    @Autowired
    private VillagerOrderMapper villagerOrderMapper;

    @Autowired
    private VillagerGoodMapper villagerGoodMapper;

    @Override
    public List<VillagerOrder> findList(VillagerOrder dto, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return villagerOrderMapper.findList(dto);
    }

    @Override
    public void add(VillagerOrder data) {
        VillagerGood good=villagerGoodMapper.selectByPrimaryKey(data.getGoodId());
        data.setOrderAmount(good.getPrice()*data.getGoodNum());
        data.setDiscountAmount(0l);
        data.setIntegralAmount(0l);
        data.setPayableAmount(data.getOrderAmount()-data.getDiscountAmount()-data.getIntegralAmount());
        data.setOrderNumber(OrderFormHelper.getRandomNum());
        data.setTimeExpire((DateUtils.addMinutes(new Date(),OrderFormHelper.ORDER_EXPIRE_MINTES*96)));//默认30分钟失效
        data.setPayStatus(OrderFormHelper.OREDER_STATUS_N);
        data.setOrderStatus(OrderFormHelper.ORDER_STATUS_WAITING_RECEIVE);
        data.setOrderType(OrderFormHelper.ORDER_TYPE_OTHER);
        villagerOrderMapper.insertSelective(data);
    }

    @Override
    public void batchSend(List<VillagerOrder> dto) {
        for (VillagerOrder t : dto) {
            t.setOrderStatus(OrderFormHelper.ORDER_STATUS_RECEIVED_GOOD);
            t.setTimeSend(new Date());
            villagerOrderMapper.updateByPrimaryKeySelective(t);
        }
    }
}