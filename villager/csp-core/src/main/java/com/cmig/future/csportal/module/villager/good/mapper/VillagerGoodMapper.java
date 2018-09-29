package com.cmig.future.csportal.module.villager.good.mapper;

import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface VillagerGoodMapper extends Mapper<VillagerGood>{

    List<VillagerGood> findList(VillagerGood data);
}