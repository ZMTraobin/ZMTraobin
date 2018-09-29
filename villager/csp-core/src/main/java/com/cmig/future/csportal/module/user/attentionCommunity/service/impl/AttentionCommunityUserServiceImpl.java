package com.cmig.future.csportal.module.user.attentionCommunity.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.mapper.AttentionCommunityUserMapper;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AttentionCommunityUserServiceImpl
 *
 * @author bubu
 *
 * 2017-3-28
 */
@Service
@Transactional
public class AttentionCommunityUserServiceImpl extends BaseServiceImpl<AttentionCommunityUser> implements IAttentionCommunityUserService {

    @Autowired
    private AttentionCommunityUserMapper attentionCommunityUserMapper;

    @Override
    @Transactional(readOnly = false)
    public void save(AttentionCommunityUser attentionCommunityUser) {
        if(!StringUtils.isEmpty(attentionCommunityUser.getId())){
            attentionCommunityUserMapper.updateByPrimaryKeySelective(attentionCommunityUser);
        }else{
            attentionCommunityUser.setId(IdGen.uuid());
            super.mapper.insertSelective(attentionCommunityUser);
        }
    }

    @Override
    public AttentionCommunityUser getEntity(AttentionCommunityUser attentionCommunityUser){
        return attentionCommunityUserMapper.getEntity(attentionCommunityUser);
    }

    @Override
    public int updateAttention(AttentionCommunityUser attentionCommunityUser) {
        attentionCommunityUserMapper.attentionByFalse(attentionCommunityUser);
        return attentionCommunityUserMapper.attentionByTrue(attentionCommunityUser);
    }

    @Override
    public int getAttentionCount(AttentionCommunityUser attentionCommunityUser) {
        return attentionCommunityUserMapper.getAttentionCount(attentionCommunityUser);
    }

    @Override
    public void delete(AttentionCommunityUser attentionCommunityUser) {
        attentionCommunityUserMapper.deleteByuser(attentionCommunityUser);
    }

    @Override
    public AttentionCommunityUser getNextData(AttentionCommunityUser attentionCommunityUser){
        return attentionCommunityUserMapper.getNextData(attentionCommunityUser);
    }

    @Override
    public List<AttentionCommunityUser> findList(AttentionCommunityUser attentionCommunityUser) {
        return attentionCommunityUserMapper.findList(attentionCommunityUser);
    }

    @Override
    public List<AttentionCommunityUser> getCommunityList(IRequest request, AttentionCommunityUser attentionCommunityUser, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return attentionCommunityUserMapper.findList(attentionCommunityUser);
    }

}