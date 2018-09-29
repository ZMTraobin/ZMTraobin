package com.cmig.future.csportal.module.properties.communityEvent.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.properties.communityEvent.dto.LjhEventCommunity;
import com.cmig.future.csportal.module.properties.communityEvent.mapper.LjhEventCommunityMapper;
import com.cmig.future.csportal.module.properties.communityEvent.service.ILjhEventCommunityService;
import com.github.pagehelper.PageHelper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LjhEventCommunityServiceImpl extends BaseServiceImpl<LjhEventCommunity>
        implements ILjhEventCommunityService {

    @Autowired
    private LjhEventCommunityMapper ljhEventCommunityMapper;

    @Override
    public List<LjhEventCommunity> selectByEvent(IRequest requestContext, LjhEventCommunity dto, int page,
            int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ljhEventCommunityMapper.selectByEvent(dto);
    }

    @Override
    public List<LjhEventCommunity> select(IRequest requestContext, LjhEventCommunity dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ljhEventCommunityMapper.select(dto);
    }

    @Override
    public List<LjhEventCommunity> submit(IRequest requestCtx, List<LjhEventCommunity> dto) {
        if (!dto.isEmpty()) {
            for(LjhEventCommunity community : dto){
                String status = community.get__status();
                if(DTOStatus.ADD.equals(status)){
                    community.setId(IdGen.uuid());
                    ljhEventCommunityMapper.insertSelective(community);
                }else{
                    
                }
            }
        }
        return dto;
    }

}