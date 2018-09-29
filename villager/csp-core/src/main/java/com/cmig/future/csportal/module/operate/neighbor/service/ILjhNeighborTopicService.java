package com.cmig.future.csportal.module.operate.neighbor.service;

import java.util.List;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighTopicView;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopic;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface ILjhNeighborTopicService extends IBaseService<LjhNeighborTopic>, ProxySelf<ILjhNeighborTopicService>{
    
    List<LjhNeighborTopic> select(IRequest requestContext, LjhNeighborTopic dto, int page, int pageSize);

    List<LjhNeighTopicView> queryById(IRequest requestContext, LjhNeighTopicView dto);

    List<LjhNeighborTopic> findList(IRequest requestContext, LjhNeighborTopic nhTopic);
    
    List<LjhNeighTopicView> queryByCondition(IRequest requestContext, LjhNeighTopicView dto, int page, int pageSize);

    com.alibaba.fastjson.JSONObject publishTopic(IRequest requestContext, LjhNeighborTopic nhTopic) throws Exception;

}