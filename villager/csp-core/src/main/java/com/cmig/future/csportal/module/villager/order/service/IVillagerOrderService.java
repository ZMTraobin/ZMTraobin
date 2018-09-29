package com.cmig.future.csportal.module.villager.order.service;

import com.cmig.future.csportal.module.villager.order.dto.VillagerOrder;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IVillagerOrderService extends IBaseService<VillagerOrder>, ProxySelf<IVillagerOrderService>{

    List<VillagerOrder> findList(VillagerOrder dto, int page, int pageSize);

    void add(VillagerOrder data);

    void batchSend(List<VillagerOrder> dto);
}