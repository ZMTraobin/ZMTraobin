package com.cmig.future.csportal.module.operate.neighbor.service;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicPraise;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface ILjhNeighborTopicPraiseService extends IBaseService<LjhNeighborTopicPraise>, ProxySelf<ILjhNeighborTopicPraiseService>{

    List<LjhNeighborTopicPraise> findList(IRequest requestContext, LjhNeighborTopicPraise nhTopicPraise);

	List<LjhNeighborTopicPraise> findListByTopicList(String userId,List<NhTopicView> nhTopicViewList);
}