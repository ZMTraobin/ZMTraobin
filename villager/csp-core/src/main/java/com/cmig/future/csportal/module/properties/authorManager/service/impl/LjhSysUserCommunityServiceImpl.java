package com.cmig.future.csportal.module.properties.authorManager.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.properties.authorManager.dto.LjhSysUserCommunity;
import com.cmig.future.csportal.module.properties.authorManager.mapper.LjhSysUserCommunityMapper;
import com.cmig.future.csportal.module.properties.authorManager.service.ILjhSysUserCommunityService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import net.sf.json.util.JSONUtils;

@Service
@Transactional
public class LjhSysUserCommunityServiceImpl extends BaseServiceImpl<LjhSysUserCommunity> implements ILjhSysUserCommunityService{

    private final Logger logger = LoggerFactory.getLogger(LjhSysUserCommunityServiceImpl.class);

    @Autowired
    LjhSysUserCommunityMapper ljhSysUserCommunityMapper;
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<LjhSysUserCommunity> saveUserCommunity(IRequest request,
            List<LjhSysUserCommunity> ljhSysUserCommunities) {
        
        logger.info("dto对象是:{}", JSONUtils.valueToString(ljhSysUserCommunities));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(ljhSysUserCommunities));
        }
        if (ljhSysUserCommunities == null || ljhSysUserCommunities.isEmpty()) {
            return ljhSysUserCommunities;
        }
        
        for (LjhSysUserCommunity ljhSysUserCommunity : ljhSysUserCommunities) {
            String status = ljhSysUserCommunity.get__status();
            if (DTOStatus.ADD.equals(status)) {
                ljhSysUserCommunity.setId(IdGen.uuid());
                ljhSysUserCommunityMapper.insertSelective(ljhSysUserCommunity);
            } else if (DTOStatus.UPDATE.equals(status)) {
                ljhSysUserCommunityMapper.updateByPrimaryKeySelective(ljhSysUserCommunity);
            }
        }
        return ljhSysUserCommunities;
    }


    @Override
    public List<LjhSysUserCommunity> selectByUser(IRequest requestContext, LjhSysUserCommunity dto, int page,
            int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ljhSysUserCommunityMapper.selectByUser(dto);
    }

    @Override
    public List<LjhSysUserCommunity> findListByUserId(LjhSysUserCommunity dto){
        return ljhSysUserCommunityMapper.findListByUserId(dto);
    }


    @Override
    public List<LjhSysUserCommunity> queryCommunitiesNoAuth(IRequest request, LjhSysUserCommunity dto, int page, int pageSize) {
        return ljhSysUserCommunityMapper.queryCommunitiesNoAuth(dto);
    }
}