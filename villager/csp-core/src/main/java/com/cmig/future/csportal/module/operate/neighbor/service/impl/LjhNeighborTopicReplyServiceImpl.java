package com.cmig.future.csportal.module.operate.neighbor.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighborTopicReplyMapper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicReply;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicReplyService;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
@Transactional
public class LjhNeighborTopicReplyServiceImpl extends BaseServiceImpl<LjhNeighborTopicReply> implements ILjhNeighborTopicReplyService {

    @Autowired
    private LjhNeighborTopicReplyMapper ljhNeighborTopicReplyMapper;

    @Override
    public List<LjhNeighborTopicReply> publishReply(IRequest requestContext, LjhNeighborTopicReply nhTopicReply) {
        nhTopicReply.setId(IdGen.uuid());
        nhTopicReply.setDelFlag(BaseExtDTO.DEL_FLAG_NORMAL);
        self().insertSelective(requestContext, nhTopicReply);
        List<LjhNeighborTopicReply> replies = self().getRepliesByCommentId(nhTopicReply.getCommentId());
        return replies;
    }

    @Override
    public List<LjhNeighborTopicReply> getRepliesByCommentId(String id) {
        LjhNeighborTopicReply reply = new LjhNeighborTopicReply();
        reply.setCommentId(id);
        List<LjhNeighborTopicReply> repliesByCommentId = ljhNeighborTopicReplyMapper.getRepliesByCommentId(reply);
        for (LjhNeighborTopicReply commentReply : repliesByCommentId) {
            //设置回复人和被回复人
            String replyId = commentReply.getReplyId();
            String commentId = commentReply.getCommentId();
            if (StringUtils.equals(replyId, commentId)) {
                LjhNeighborTopicReply replyTo = ljhNeighborTopicReplyMapper.getReplyTo(commentReply.getId());
                if(replyTo!=null){
                    commentReply.setReplyTo(replyTo.getReplyTo());
                    commentReply.setReplyToIcon(replyTo.getReplyToIcon());
                }

            }
        }
        return repliesByCommentId;
    }

    @Override
    public void deleteReply(LjhNeighborTopicReply reply) {
        //1.是否递归删除2.是否硬删除
        reply.setDelFlag(BaseExtDTO.DEL_FLAG_DELETE);
        self().updateByPrimaryKeySelective(null, reply);
    }

    @Override
    public void deleteReply(IRequest requestContext, LjhNeighborTopicReply reply) {
        //1.是否递归删除2.是否硬删除
        reply.setDelFlag(BaseExtDTO.DEL_FLAG_DELETE);
        self().updateByPrimaryKeySelective(requestContext, reply);
    }
}