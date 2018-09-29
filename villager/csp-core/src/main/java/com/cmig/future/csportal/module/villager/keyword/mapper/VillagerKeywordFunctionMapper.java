package com.cmig.future.csportal.module.villager.keyword.mapper;

import com.cmig.future.csportal.module.villager.keyword.dto.VillagerKeywordFunction;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface VillagerKeywordFunctionMapper extends Mapper<VillagerKeywordFunction>{

    List<VillagerKeywordFunction> findByKeyWord(VillagerKeywordFunction villagerKeywordFunction);

}