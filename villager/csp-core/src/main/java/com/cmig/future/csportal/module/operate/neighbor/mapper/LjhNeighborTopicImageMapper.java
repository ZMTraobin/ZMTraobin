package com.cmig.future.csportal.module.operate.neighbor.mapper;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicImage;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface LjhNeighborTopicImageMapper extends Mapper<LjhNeighborTopicImage>{
    
    List<LjhNeighborTopicImage> findList(LjhNeighborTopicImage ljhNeighborTopicImage);
    
   int delete(LjhNeighborTopicImage ljhNeighborTopicImage);

	List<LjhNeighborTopicImage> findListByTopicList(List<NhTopicView> list);
}