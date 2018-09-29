package com.cmig.future.csportal.module.operate.neighbor.service;

import com.alibaba.fastjson.JSONObject;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicComment;

public interface ILjhNeighborTopicCommentService extends IBaseService<LjhNeighborTopicComment>, ProxySelf<ILjhNeighborTopicCommentService>{

    JSONObject publishComment(IRequest requestContext, LjhNeighborTopicComment nhTopicComment) throws Exception;

    void deleteComment(LjhNeighborTopicComment comment);
}