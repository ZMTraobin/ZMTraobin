package com.cmig.future.csportal.module.operate.neighbor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.constant.TopicType;
import com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation;
import com.cmig.future.csportal.module.operate.neighbor.dto.*;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighTopicViewMapper;
import com.cmig.future.csportal.module.operate.neighbor.mapper.LjhNeighborTopicMapper;
import com.cmig.future.csportal.module.operate.neighbor.mapper.NhTopicViewMapper;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicImageService;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicReplyService;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicService;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.github.pagehelper.PageHelper;
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

public class LjhNeighborTopicServiceImpl extends BaseServiceImpl<LjhNeighborTopic> implements ILjhNeighborTopicService {

    private static Log logger = LogFactory.getLog(LjhNeighborTopicServiceImpl.class);

    @Autowired
    private LjhNeighborTopicMapper ljhNeighborTopicMapper;
    @Autowired
    private NhTopicViewMapper nhTopicViewMapper;
    @Autowired
    private LjhNeighTopicViewMapper ljhNeighTopicViewMapper;
    @Autowired
    private ILjhNeighborTopicImageService nhTopicImageService;
    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private IntegralRuleOperation integralRuleOperation;
    @Autowired
    private ILjhNeighborTopicReplyService ljhNeighborTopicReplyService;

    // 创建一个可重用固定线程数的线程池
    static ExecutorService pool = Executors.newFixedThreadPool(2);

    @Override
    @Transactional
    public List<LjhNeighborTopic> select(IRequest requestContext, LjhNeighborTopic dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ljhNeighborTopicMapper.select(dto);
    }

    @Override
    @Transactional
    public List<LjhNeighTopicView> queryByCondition(IRequest requestContext, LjhNeighTopicView dto, int page,
                                                    int pageSize) {
        PageHelper.startPage(page, pageSize);
        return nhTopicViewMapper.queryByCondition(dto);
    }

    @Override
    @Transactional
    public List<LjhNeighTopicView> queryById(IRequest requestContext, LjhNeighTopicView dto) {
        List<LjhNeighTopicView> topics = ljhNeighTopicViewMapper.queryById(dto);
        if (topics != null && !topics.isEmpty()) {
            for (LjhNeighTopicView topic : topics) {
                List<LjhNeighborTopicComment> comments = ljhNeighTopicViewMapper.queryCommentsByTopicId(topic.getId());
                if(!comments.isEmpty()){
                    comments.forEach(comment -> {
                        List<LjhNeighborTopicReply> replies = ljhNeighborTopicReplyService.getRepliesByCommentId(comment.getId());
                        comment.setReplies(replies);
                    });
                }
                topic.setComments(comments);
            }
        }
        return topics;
    }

    @Override
    @Transactional
    public List<LjhNeighborTopic> findList(IRequest requestContext, LjhNeighborTopic nhTopic) {
        // TODO Auto-generated method stub
        return ljhNeighborTopicMapper.select(nhTopic);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JSONObject publishTopic(IRequest requestContext, LjhNeighborTopic nhTopic) {
        if(TopicType.video.getCode().equals(nhTopic.getTopicType())){

        }else if(StringUtils.isNotEmpty(nhTopic.getUrls())){
            nhTopic.setTopicType(TopicType.image.getCode());
        }else{
            nhTopic.setTopicType(TopicType.text.getCode());
        }

        // 话题
        self().insertSelective(requestContext, nhTopic);
        // 图片
        int imgCount = 0;
        if (StringUtils.isNotEmpty(nhTopic.getUrls())) {
            logger.info("urls:" + nhTopic.getUrls());
            String[] urlArr = nhTopic.getUrls().split(",");
            LjhNeighborTopicImage nhTopicImage = null;
            imgCount = urlArr.length;
            for (String url : urlArr) {
                nhTopicImage = new LjhNeighborTopicImage();
                nhTopicImage.setId(IdGen.uuid());
                nhTopicImage.setTopicId(nhTopic.getId());
                nhTopicImage.setUrl(url);
                nhTopicImageService.insertSelective(requestContext, nhTopicImage);
            }
        }
        // 积分处理
        String token = nhTopic.getToken();
        String topicContent = nhTopic.getTopicContent();
        String regEx = "[\\u4e00-\\u9fa5]";
        String term = topicContent.replaceAll(regEx, "aa");
        Integer count = term.length() - topicContent.length();
        Map<String, String> map = new HashMap<String, String>();
        AppUser appUser = appUserService.getByToken(token);
        map.put("app_id", Global.getConfig("INTEGRAL.APP_ID"));
        map.put("service_id", Global.getConfig("INTEGRAL_SERVER_ID"));
        map.put("uid", appUser.getSourceSystemId());
        map.put("phone", appUser.getMobile());
        // 场景类型
        map.put("type", "A0007");
        // 消息发布字数
        map.put("msg_length", count.toString());
        // 消息图片数量
        if (imgCount > 0) {
            map.put("pic_num", String.valueOf(imgCount));
        }

        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        // 是否首评
        //map.put("first_comment", appUser.getMobile());
        try {
            return integralRuleOperation.addIntegral(map);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("returnCode","1");
            obj.put("message","调用积分操作失败，积分无变动");
            return obj;
        }
    }

//    public void updateUserIntegral(Map<String, String> map) throws Exception {
//        Runnable r = new Runnable() {
//            public void run() {
//                try {
//        JSONObject result = integralRuleOperation.addIntegral(map);
//                } catch (Exception e) {
//                    logger.info("更新积分失败");
//                    e.printStackTrace();
//                }
//            }
//        };
//        pool.execute(r);
//    }

}