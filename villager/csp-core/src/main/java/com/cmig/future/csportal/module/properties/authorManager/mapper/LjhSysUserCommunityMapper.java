package com.cmig.future.csportal.module.properties.authorManager.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.cmig.future.csportal.module.properties.authorManager.dto.LjhSysUserCommunity;

public interface LjhSysUserCommunityMapper extends Mapper<LjhSysUserCommunity>{

    List<LjhSysUserCommunity> selectByUser(LjhSysUserCommunity dto);

    List<LjhSysUserCommunity> findListByUserId(LjhSysUserCommunity dto);

    List<LjhSysUserCommunity> queryCommunitiesNoAuth(LjhSysUserCommunity dto);
}