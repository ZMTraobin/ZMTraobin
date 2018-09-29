package com.cmig.future.csportal.module.properties.communityEvent.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.cmig.future.csportal.module.properties.communityEvent.dto.LjhEventCommunity;

public interface LjhEventCommunityMapper extends Mapper<LjhEventCommunity>{

    List<LjhEventCommunity> selectByEvent(LjhEventCommunity dto);
    
    List<LjhEventCommunity> select(LjhEventCommunity dto);

}