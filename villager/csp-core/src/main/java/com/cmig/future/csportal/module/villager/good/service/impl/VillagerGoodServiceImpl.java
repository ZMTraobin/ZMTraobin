package com.cmig.future.csportal.module.villager.good.service.impl;

import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.cmig.future.csportal.module.villager.good.mapper.VillagerGoodMapper;
import com.cmig.future.csportal.module.villager.good.service.IVillagerGoodService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VillagerGoodServiceImpl extends BaseServiceImpl<VillagerGood> implements IVillagerGoodService{

    @Autowired
    private VillagerGoodMapper villagerGoodMapper;

    @Override
    public List<VillagerGood> findList(VillagerGood data, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return villagerGoodMapper.findList(data);
    }
}