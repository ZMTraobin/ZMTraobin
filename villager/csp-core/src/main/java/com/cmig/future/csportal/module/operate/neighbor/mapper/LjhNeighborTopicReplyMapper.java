package com.cmig.future.csportal.module.operate.neighbor.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicReply;

import java.util.List;

public interface LjhNeighborTopicReplyMapper extends Mapper<LjhNeighborTopicReply>{

    List<LjhNeighborTopicReply> getRepliesByCommentId(LjhNeighborTopicReply reply);

    LjhNeighborTopicReply getReplyTo(String id);

}