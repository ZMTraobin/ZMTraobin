package com.cmig.future.csportal.module.properties.communityEvent.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.properties.communityEvent.dto.LjhEventCommunity;

public interface ILjhEventCommunityService extends IBaseService<LjhEventCommunity>, ProxySelf<ILjhEventCommunityService>{

    List<LjhEventCommunity> selectByEvent(IRequest requestContext, LjhEventCommunity dto, int page, int pageSize);
    
    List<LjhEventCommunity> select(IRequest requestContext, LjhEventCommunity dto, int page, int pageSize);

    List<LjhEventCommunity> submit(IRequest requestCtx, List<LjhEventCommunity> dto);

}