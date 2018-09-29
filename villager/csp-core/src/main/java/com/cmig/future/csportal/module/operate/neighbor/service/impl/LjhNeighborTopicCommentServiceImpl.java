package com.cmig.future.csportal.module.operate.neighbor.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicComment;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicReply;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighborTopicCommentMapper;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighborTopicReplyMapper;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicCommentService;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service

public class LjhNeighborTopicCommentServiceImpl extends BaseServiceImpl<LjhNeighborTopicComment>
        implements ILjhNeighborTopicCommentService {

    private static Log logger = LogFactory.getLog(LjhNeighborTopicServiceImpl.class);
    // 创建一个可重用固定线程数的线程池
    static ExecutorService pool = Executors.newFixedThreadPool(2);

    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private LjhNeighborTopicCommentMapper ljhNeighborTopicCommentMapper;
    @Autowired
    private IntegralRuleOperation integralRuleOperation;
    @Autowired
    private LjhNeighborTopicReplyMapper ljhNeighborTopicReplyMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JSONObject publishComment(IRequest requestContext, LjhNeighborTopicComment nhTopicComment) {
        // 积分
        String token = nhTopicComment.getToken();
        String comment = nhTopicComment.getContent();
        String regEx = "[\\u4e00-\\u9fa5]";
        String term = comment.replaceAll(regEx, "aa");
        Integer count = term.length() - comment.length();

        Map<String, String> map = new HashMap<String, String>();
        AppUser appUser = appUserService.getByToken(token);
        map.put("app_id", Global.getConfig("INTEGRAL.APP_ID"));
        map.put("service_id", Global.getConfig("INTEGRAL_SERVER_ID"));
        map.put("uid", appUser.getSourceSystemId());
        map.put("phone", appUser.getMobile());
        // 场景类型
        map.put("type", "A0008");
        // 消息发布字数
        map.put("msg_length", count.toString());
        // 是否首评
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        // 查询话题下面是否有评论
        String topicId = nhTopicComment.getTopicId();
        int hasComments = ljhNeighborTopicCommentMapper.hasComments(topicId);
        if (hasComments > 0) {
            map.put("first_comment", "false");
        } else {
            map.put("first_comment", "true");
        }

        self().insertSelective(requestContext, nhTopicComment);

        JSONObject jsonObject = null;
        try {
            return integralRuleOperation.addIntegral(map);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("returnCode", "1");
            obj.put("message", "调用积分操作失败，积分无变动");
            return obj;
        }
    }

    @Override
    public void deleteComment(LjhNeighborTopicComment comment) {
        //先删除评论，再删除评论的回复
        String commentId = comment.getId();
        comment.setDelFlag(BaseExtDTO.DEL_FLAG_DELETE);
        self().updateByPrimaryKeySelective(null, comment);
        LjhNeighborTopicReply commmentReply = new LjhNeighborTopicReply();
        commmentReply.setCommentId(commentId);
        List<LjhNeighborTopicReply> replies = ljhNeighborTopicReplyMapper.getRepliesByCommentId(commmentReply);
        replies.forEach(reply -> {
            reply.setDelFlag(BaseExtDTO.DEL_FLAG_DELETE);
            ljhNeighborTopicReplyMapper.updateByPrimaryKeySelective(reply);
        });

    }


}