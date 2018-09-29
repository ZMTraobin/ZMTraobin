package com.cmig.future.csportal.module.villager.order.service;

import com.cmig.future.csportal.module.villager.order.dto.VillagerVoiceOrder;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IVillagerVoiceOrderService extends IBaseService<VillagerVoiceOrder>, ProxySelf<IVillagerVoiceOrderService>{

    List<VillagerVoiceOrder> findList(VillagerVoiceOrder dto, int page, int pageSize);

    void add(VillagerVoiceOrder data);

    void batchSend(List<VillagerVoiceOrder> dto);

    void confirmReceipt(String appUserId, Long id);

    void received(String mgtUserId, Long id);

    List<VillagerVoiceOrder> findAdminList(VillagerVoiceOrder villagerVoiceOrder, int page, int pageSize);
}