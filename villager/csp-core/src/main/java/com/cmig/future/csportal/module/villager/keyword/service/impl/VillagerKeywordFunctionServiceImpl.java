package com.cmig.future.csportal.module.villager.keyword.service.impl;

import com.cmig.future.csportal.module.villager.keyword.mapper.VillagerKeywordFunctionMapper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.keyword.dto.VillagerKeywordFunction;
import com.cmig.future.csportal.module.villager.keyword.service.IVillagerKeywordFunctionService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VillagerKeywordFunctionServiceImpl extends BaseServiceImpl<VillagerKeywordFunction> implements IVillagerKeywordFunctionService{

    @Autowired
    private VillagerKeywordFunctionMapper villagerKeywordFunctionMapper;

    @Override
    public List<VillagerKeywordFunction> findByKeyWord(String keyword) {
        VillagerKeywordFunction villagerKeywordFunction=new VillagerKeywordFunction();
        villagerKeywordFunction.setKeyword(keyword);
        return villagerKeywordFunctionMapper.findByKeyWord(villagerKeywordFunction);
    }
}