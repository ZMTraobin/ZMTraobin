package com.cmig.future.csportal.module.operate.neighbor.service.impl;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicImage;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighborTopicImageMapper;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicImageService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LjhNeighborTopicImageServiceImpl extends BaseServiceImpl<LjhNeighborTopicImage> implements ILjhNeighborTopicImageService{

    @Autowired
    private LjhNeighborTopicImageMapper ljhNeighborTopicImageMapper;
    
    @Override
    public List<LjhNeighborTopicImage> findList(IRequest requestContext, LjhNeighborTopicImage nhTopicImage) {
        return ljhNeighborTopicImageMapper.findList(nhTopicImage);
    }

	@Override
	public List<LjhNeighborTopicImage> findListByTopicList(List<NhTopicView> list) {
		return ljhNeighborTopicImageMapper.findListByTopicList(list);
	}
}