package com.cmig.future.csportal.module.cooperate.service.impl;

import com.cmig.future.csportal.module.cooperate.dto.Cooperate;
import com.cmig.future.csportal.module.cooperate.mapper.CooperateMapper;
import com.cmig.future.csportal.module.cooperate.service.ICooperateService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CooperateServiceImpl extends BaseServiceImpl<Cooperate> implements ICooperateService {
    @Autowired
    private CooperateMapper cooperateMapper;

    /**
     * 商户新增
     * @param cooperate
     */
    @Override
    public void save(Cooperate cooperate) {
        cooperateMapper.insertCooperate(cooperate);
    }

    /**
     * 根据AppUserID查询该用户下的所有商户
     * @param cooperate
     * @return
     */
    @Override
    public List<Cooperate> selectCooperateByAppUserId(Cooperate cooperate) {
        return cooperateMapper.selectCooperateByAppUserId(cooperate);
    }

    /**
     * 分页查询所有的商户
     * @param request
     * @param cooperate
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Cooperate> selectAllCooperate(IRequest request, Cooperate cooperate, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return cooperateMapper.selectAllCooperate(cooperate);
    }
}