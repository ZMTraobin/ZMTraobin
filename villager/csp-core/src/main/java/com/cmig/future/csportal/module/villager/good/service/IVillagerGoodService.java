package com.cmig.future.csportal.module.villager.good.service;

import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IVillagerGoodService extends IBaseService<VillagerGood>, ProxySelf<IVillagerGoodService>{

    List<VillagerGood> findList(VillagerGood data, int page, int pageSize);
}