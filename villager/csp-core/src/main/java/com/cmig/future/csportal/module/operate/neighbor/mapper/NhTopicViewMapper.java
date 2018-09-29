package com.cmig.future.csportal.module.operate.neighbor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighTopicView;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;

@Repository
public interface NhTopicViewMapper{

    List<NhTopicView> select(NhTopicView nhTopicView);

    NhTopicView get(String id);
    
    List<NhTopicView> findList(NhTopicView nhTopicView);
    
    List<NhTopicView> findListApp(NhTopicView nhTopicView);
    
    List<NhTopicView> findListMyTopic(NhTopicView nhTopicView);
    
    List<NhTopicView> findAllList(NhTopicView nhTopicView);

    List<LjhNeighTopicView> queryByCondition(LjhNeighTopicView dto);

    void updateTopic(NhTopicView nhTopicView);
     

}