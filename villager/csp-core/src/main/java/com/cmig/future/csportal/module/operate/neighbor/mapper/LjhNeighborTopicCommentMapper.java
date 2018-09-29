package com.cmig.future.csportal.module.operate.neighbor.mapper;

import com.hand.hap.mybatis.common.Mapper;

import org.apache.ibatis.annotations.Param;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicComment;

public interface LjhNeighborTopicCommentMapper extends Mapper<LjhNeighborTopicComment>{
    
  //是否首评
    int hasComments(@Param(value = "topicId") String topicId);

}