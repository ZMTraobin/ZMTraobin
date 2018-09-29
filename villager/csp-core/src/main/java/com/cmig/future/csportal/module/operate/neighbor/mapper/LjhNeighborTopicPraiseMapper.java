package com.cmig.future.csportal.module.operate.neighbor.mapper;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicPraise;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LjhNeighborTopicPraiseMapper extends Mapper<LjhNeighborTopicPraise>{
    
    List<LjhNeighborTopicPraise> findList(LjhNeighborTopicPraise ljhNeighborTopicPraise);
    
    int delete(LjhNeighborTopicPraise ljhNeighborTopicPraise);

	List<LjhNeighborTopicPraise> findListByTopicList(@Param(value = "userId") String userId, @Param(value = "nhTopicViewList")  List<NhTopicView> nhTopicViewList);
}