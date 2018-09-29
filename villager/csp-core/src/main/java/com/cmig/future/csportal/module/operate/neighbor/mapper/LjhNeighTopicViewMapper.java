package com.cmig.future.csportal.module.operate.neighbor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighTopicView;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicComment;

@Repository
public interface LjhNeighTopicViewMapper{

    List<LjhNeighTopicView> queryById(LjhNeighTopicView dto); 
    
    List<LjhNeighborTopicComment> queryCommentsByTopicId(String topicId);

}