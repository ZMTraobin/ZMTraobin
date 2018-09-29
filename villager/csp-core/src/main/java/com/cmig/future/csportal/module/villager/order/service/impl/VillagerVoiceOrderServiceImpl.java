package com.cmig.future.csportal.module.villager.order.service.impl;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.util.CommunityHelper;
import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.cmig.future.csportal.module.villager.good.mapper.VillagerGoodMapper;
import com.cmig.future.csportal.module.villager.order.dto.VillagerVoiceOrder;
import com.cmig.future.csportal.module.villager.order.mapper.VillagerVoiceOrderMapper;
import com.cmig.future.csportal.module.villager.order.service.IVillagerVoiceOrderService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VillagerVoiceOrderServiceImpl extends BaseServiceImpl<VillagerVoiceOrder> implements IVillagerVoiceOrderService{

    @Autowired
    private VillagerVoiceOrderMapper villagerVoiceOrderMapper;

    @Autowired
    private VillagerGoodMapper villagerGoodMapper;

    @Autowired
    private IAttentionCommunityUserService attentionCommunityUserService;


    @Override
    public List<VillagerVoiceOrder> findList(VillagerVoiceOrder dto, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return villagerVoiceOrderMapper.findList(dto);
    }

    @Override
    public void add(VillagerVoiceOrder data) {
        //当前关注的小区信息
        AttentionCommunityUser attentionCommunityUser = new AttentionCommunityUser();
        attentionCommunityUser.setUserId(data.getAppUserId());
        attentionCommunityUser.setIsAttention(CommunityHelper.IS_ATTENTION);
        List<AttentionCommunityUser> list = attentionCommunityUserService.findList(attentionCommunityUser);
        if (list != null && list.size() > 0) {
            AttentionCommunityUser thisAttentionCommunityUser = list.get(0);
            data.setCommunityId(thisAttentionCommunityUser.getCommunityId());
        }

        VillagerGood good=villagerGoodMapper.selectByPrimaryKey(data.getGoodId());
        data.setGoodName(good.getGoodName());
        data.setGoodNum(1l);
        data.setOrderAmount(good.getPrice()*data.getGoodNum());
        data.setDiscountAmount(0l);
        data.setIntegralAmount(0l);
        data.setPayableAmount(data.getOrderAmount()-data.getDiscountAmount()-data.getIntegralAmount());
        data.setOrderNumber(OrderFormHelper.getRandomNum());
        data.setTimeExpire((DateUtils.addMinutes(new Date(),OrderFormHelper.ORDER_EXPIRE_MINTES*96)));//默认30分钟失效
        data.setPayStatus(OrderFormHelper.OREDER_STATUS_N);
        data.setOrderStatus(OrderFormHelper.ORDER_STATUS_WAITING_RECEIVE);
        data.setOrderType(OrderFormHelper.ORDER_TYPE_OTHER);
        villagerVoiceOrderMapper.insertSelective(data);
    }

    @Override
    public void batchSend(List<VillagerVoiceOrder> dto) {
        for (VillagerVoiceOrder t : dto) {
            t.setOrderStatus(OrderFormHelper.ORDER_STATUS_RECEIVED_GOOD);
            t.setTimeSend(new Date());
            villagerVoiceOrderMapper.updateByPrimaryKeySelective(t);
        }
    }

    @Override
    public void confirmReceipt(String appUserId, Long id) {
        villagerVoiceOrderMapper.confirmReceipt(appUserId,id);
    }

    @Override
    public void received(String mgtUserId, Long id) {
        villagerVoiceOrderMapper.received(mgtUserId,id);
    }

    @Override
    public List<VillagerVoiceOrder> findAdminList(VillagerVoiceOrder villagerVoiceOrder, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        return villagerVoiceOrderMapper.findAdminList(villagerVoiceOrder);
    }
}