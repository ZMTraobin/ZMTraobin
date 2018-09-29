package com.cmig.future.csportal.module.operate.neighbor.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicReply;

import java.util.List;

public interface ILjhNeighborTopicReplyService extends IBaseService<LjhNeighborTopicReply>, ProxySelf<ILjhNeighborTopicReplyService>{

    List<LjhNeighborTopicReply> publishReply(IRequest requestContext, LjhNeighborTopicReply nhTopicReply);

    List<LjhNeighborTopicReply> getRepliesByCommentId(String id);

    void deleteReply(LjhNeighborTopicReply reply);

    void deleteReply(IRequest requestContext, LjhNeighborTopicReply reply);
}