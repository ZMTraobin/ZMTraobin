package com.cmig.future.csportal.module.operate.neighbor.service;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicImage;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface ILjhNeighborTopicImageService extends IBaseService<LjhNeighborTopicImage>, ProxySelf<ILjhNeighborTopicImageService>{

    List<LjhNeighborTopicImage> findList(IRequest requestContext, LjhNeighborTopicImage nhTopicImage);

	List<LjhNeighborTopicImage> findListByTopicList(List<NhTopicView> list);
}