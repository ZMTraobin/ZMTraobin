package com.cmig.future.csportal.module.villager.keyword.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.villager.keyword.dto.VillagerKeywordFunction;

import java.util.List;

public interface IVillagerKeywordFunctionService extends IBaseService<VillagerKeywordFunction>, ProxySelf<IVillagerKeywordFunctionService>{

    List<VillagerKeywordFunction> findByKeyWord(String keyword);
}