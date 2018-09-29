package com.cmig.future.csportal.module.operate.neighbor.service.impl;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicPraise;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighborTopicPraiseMapper;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicPraiseService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LjhNeighborTopicPraiseServiceImpl extends BaseServiceImpl<LjhNeighborTopicPraise> implements ILjhNeighborTopicPraiseService{

    @Autowired
    private LjhNeighborTopicPraiseMapper ljhNeighborTopicPraiseMapper;
    
    @Override
    public List<LjhNeighborTopicPraise> findList(IRequest requestContext, LjhNeighborTopicPraise nhTopicPraise) {
        return ljhNeighborTopicPraiseMapper.findList(nhTopicPraise);
    }

	@Override
	public List<LjhNeighborTopicPraise> findListByTopicList(String userId, List<NhTopicView> nhTopicViewList) {
		return ljhNeighborTopicPraiseMapper.findListByTopicList(userId,nhTopicViewList);
	}
}