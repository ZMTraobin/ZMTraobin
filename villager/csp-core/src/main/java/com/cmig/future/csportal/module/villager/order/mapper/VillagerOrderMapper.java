package com.cmig.future.csportal.module.villager.order.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.villager.order.dto.VillagerOrder;

import java.util.List;

public interface VillagerOrderMapper extends Mapper<VillagerOrder>{

    List<VillagerOrder> findList(VillagerOrder dto);
}