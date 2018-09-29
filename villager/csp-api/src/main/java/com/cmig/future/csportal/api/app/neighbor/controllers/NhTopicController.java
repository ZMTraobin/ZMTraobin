package com.cmig.future.csportal.api.app.neighbor.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.AppPage;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopic;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicComment;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicImage;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicPraise;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicReply;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicCommentView;
import com.cmig.future.csportal.module.operate.neighbor.dto.NhTopicView;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicCommentService;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicImageService;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicPraiseService;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicReplyService;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicService;
import com.cmig.future.csportal.module.operate.neighbor.service.NhTopicCommentViewService;
import com.cmig.future.csportal.module.operate.neighbor.service.NhTopicViewService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.hand.hap.core.IRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${userPath}")
public class NhTopicController extends BaseExtendController {

    @Autowired
    private ILjhNeighborTopicImageService nhTopicImageService;
    @Autowired
    private ILjhNeighborTopicService nhTopicService;
    @Autowired
    private NhTopicViewService nhTopicViewService;
    @Autowired
    private NhTopicCommentViewService nhTopicCommentViewService;
    @Autowired
    private ILjhNeighborTopicPraiseService nhTopicPraiseService;
    @Autowired
    private ILjhNeighborTopicCommentService nhTopicCommentService;
    @Autowired
    private ILjhNeighborTopicReplyService nhTopicReplyService;

    @RequestMapping(value = "/st/neighborhood/test")
    public String text() {
        return "ok";
    }

    /**
     * 此方法描述的是：发布话题——已登录
     *
     * @param nhTopic
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/st/neighborhood/publishTopic", produces = {"application/json"}, method = RequestMethod.POST)
    public String publishTopic(LjhNeighborTopic nhTopic,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        IRequest requestContext = createRequestContext(request);

        //检验话题内容
        if (StringUtils.isBlank(nhTopic.getTopicContent())) {
            RetApp retApp = new RetApp(FAIL, "话题内容为空！", "");
            return renderString(response, retApp);
        }

        //检验分类ID
        if (StringUtils.isBlank(nhTopic.getTypeId())) {
            RetApp retApp = new RetApp(FAIL, "分类不能为空!", "");
            return renderString(response, retApp);
        }
        //检验话题小区ID
        if (StringUtils.isBlank(nhTopic.getCommunityId())) {
            RetApp retApp = new RetApp(FAIL, "小区ID为空!", "");
            return renderString(response, retApp);
        }
        //获取token
        String token = nhTopic.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        //发布话题
        nhTopic.setId(IdGen.uuid());
        nhTopic.setPublishId(userId);
        nhTopic.setPublishTime(new Date());
        JSONObject jsonObject = nhTopicService.publishTopic(requestContext, nhTopic);

        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("发布成功");
        JSONObject integral = new JSONObject();
        integral.put("integralResult", jsonObject);
        retApp.setData(integral);
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：话题详情——未登录
     *
     * @param nhTopicView
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/neighborhood/topicView", produces = {"application/json"}, method = RequestMethod.POST)
    public String topicView(NhTopicView nhTopicView,
                            HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        //检验ID
        if (StringUtils.isBlank(nhTopicView.getId())) {
            RetApp retApp = new RetApp(FAIL, "话题ID为空!", "");
            return renderString(response, retApp);
        }
        //查询话题信息
        nhTopicView = nhTopicViewService.get(nhTopicView.getId());
        if (nhTopicView != null) {
            //String imagePath=Global.getImageServerPath();
            //处理头像路径
            nhTopicView.setUserIcon(Global.getFullImagePathForReduce(nhTopicView.getUserIcon()));
            //获取话题评论信息
            NhTopicCommentView nhTopicCommentView = new NhTopicCommentView();
            nhTopicCommentView.setTopicId(nhTopicView.getId());
            AppPage<NhTopicCommentView> page = new AppPage<NhTopicCommentView>();
            page.setPageSize(10);
            AppPage<NhTopicCommentView> comments = nhTopicCommentViewService.findPage(requestContext, page, nhTopicCommentView);
            comments = getCommentPath(comments);
            nhTopicView.setComments(comments);
            //图片处理
            List<LjhNeighborTopicImage> images = nhTopicView.getMedias();
            images = getImageServerPath(images);
            nhTopicView.setMedias(images);
        } else {
            RetApp retApp = new RetApp(FAIL, "该话题不存在!", "");
            return renderString(response, retApp);
        }
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        System.out.println("typeId:" + nhTopicView.getTypeId());
        System.out.println("type name:" + nhTopicView.getTypeName());
        retApp.setData(nhTopicView);
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：话题详情——已登录
     *
     * @param nhTopicView
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/st/neighborhood/topicView", produces = {"application/json"}, method = RequestMethod.POST)
    public String topicViewOn(NhTopicView nhTopicView,
                              HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        //检验ID
        if (StringUtils.isBlank(nhTopicView.getId())) {
            RetApp retApp = new RetApp(FAIL, "话题ID为空!", "");
            return renderString(response, retApp);
        }
        //获取token
        String token = nhTopicView.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        //查询话题信息
        nhTopicView = nhTopicViewService.get(nhTopicView.getId());
        if (nhTopicView != null) {
            //处理头像路径
            nhTopicView.setUserIcon(Global.getFullImagePathForReduce(nhTopicView.getUserIcon()));
            //获取话题评论信息
            NhTopicCommentView nhTopicCommentView = new NhTopicCommentView();
            nhTopicCommentView.setTopicId(nhTopicView.getId());
            AppPage<NhTopicCommentView> page = new AppPage<NhTopicCommentView>();
            page.setPageSize(10);
            AppPage<NhTopicCommentView> comments = nhTopicCommentViewService.findPage(requestContext, page, nhTopicCommentView);
            comments = getCommentPath(comments);
            nhTopicView.setComments(comments);
            //图片处理
            List<LjhNeighborTopicImage> images = nhTopicView.getMedias();
            images = getImageServerPath(images);
            nhTopicView.setMedias(images);
        } else {
            RetApp retApp = new RetApp(FAIL, "该话题不存在!", "");
            return renderString(response, retApp);
        }
        //处理当前点赞状态
        LjhNeighborTopicPraise nhTopicPraise = new LjhNeighborTopicPraise();
        nhTopicPraise.setTopicId(nhTopicView.getId());
        nhTopicPraise.setUserId(userId);
        List<LjhNeighborTopicPraise> nhTopicPraises = nhTopicPraiseService.findList(requestContext, nhTopicPraise);
        if (nhTopicPraises != null && nhTopicPraises.size() > 0) {
            nhTopicView.setPraiseFlag(true);
        } else {
            nhTopicView.setPraiseFlag(false);
        }

        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(nhTopicView);
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：话题列表（全部）（分页）——未登录
     *
     * @param page
     * @param nhTopicView
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/neighborhood/allTopicList", produces = {"application/json"}, method = RequestMethod.POST)
    public String allTopicList(AppPage<NhTopicView> page, NhTopicView nhTopicView,
                               HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        //关注小区处理
        if (StringUtils.isNotBlank(nhTopicView.getCommunityIds())) {
            String[] ids = nhTopicView.getCommunityIds().split(",");
            //暂时屏蔽掉关注小区的处理
            nhTopicView.setIds(ids);
        }
        //查询话题信息
        AppPage<NhTopicView> nhTopicViews = nhTopicViewService.findPageApp(requestContext, page, nhTopicView);
        //获取话题信息中图片信息
        nhTopicViews = getNhTopicImages(nhTopicViews, null);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(nhTopicViews);
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：话题列表（全部）（分页）——已登录
     *
     * @param page
     * @param nhTopicView
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/st/neighborhood/allTopicList", produces = {"application/json"}, method = RequestMethod.POST)
    public String allTopicListOn(AppPage<NhTopicView> page, NhTopicView nhTopicView,
                                 HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        //获取token
        String token = nhTopicView.getToken();
        //关注小区处理
        if (StringUtils.isNotBlank(nhTopicView.getCommunityIds())) {
            String[] ids = nhTopicView.getCommunityIds().split(",");
            nhTopicView.setIds(ids);
        }
        //查询话题信息
        AppPage<NhTopicView> nhTopicViews = nhTopicViewService.findPageApp(requestContext, page, nhTopicView);

        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        //获取话题信息中图片信息
        nhTopicViews = getNhTopicImages(nhTopicViews, userId);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(nhTopicViews);
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：话题列表（我的话题）（分页）——已登录
     *
     * @param page
     * @param nhTopicView
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/st/neighborhood/myTopicList", produces = {"application/json"}, method = RequestMethod.POST)
    public String myTopicList(AppPage<NhTopicView> page, NhTopicView nhTopicView,
                              HttpServletRequest request, HttpServletResponse response) {
        //获取token
        String token = nhTopicView.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        nhTopicView.setPublishId(userId);
        //查询话题信息
        AppPage<NhTopicView> nhTopicViews = nhTopicViewService.findPageMyTopic(page, nhTopicView);
        //获取话题信息中图片信息
        nhTopicViews = getNhTopicImages(nhTopicViews, null);

        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(nhTopicViews);
        return renderString(response, retApp);
    }

    /**
     * 删除我的话题
     *
     * @param
     * @param nhTopicView
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/st/neighborhood/deleteTopic", produces = {"application/json"}, method = RequestMethod.POST)
    public String deleteTopic(NhTopicView nhTopicView,
                              HttpServletRequest request, HttpServletResponse response) {
        //获取token
        String token = nhTopicView.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        nhTopicView.setPublishId(userId);
        nhTopicViewService.updateTopic(nhTopicView);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("删除成功");
        return renderString(response, retApp);
    }

    /**
     * 删除回复
     *
     * @param reply
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/neighborhood/deleteReply", produces = {"application/json"}, method = RequestMethod.POST)
    public String deleteReply(LjhNeighborTopicReply reply,
                              HttpServletRequest request, HttpServletResponse response) {
        //检验ID
        if (StringUtils.isBlank(reply.getId())) {
            RetApp retApp = new RetApp(FAIL, "回复ID为空!", "");
            return renderString(response, retApp);
        }
        //获取token
        String token = reply.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        reply.setUserId(userId);
        nhTopicReplyService.deleteReply(reply);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("删除成功");
        return renderString(response, retApp);
    }

    /**
     * 删除评论
     *
     * @param comment
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/neighborhood/deleteComment", produces = {"application/json"}, method = RequestMethod.POST)
    public String deleteComment(LjhNeighborTopicComment comment,
                                HttpServletRequest request, HttpServletResponse response) {
        //检验ID
        if (StringUtils.isBlank(comment.getId())) {
            RetApp retApp = new RetApp(FAIL, "评论ID为空!", "");
            return renderString(response, retApp);
        }

        //获取token
        String token = comment.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        comment.setUserId(userId);
        nhTopicCommentService.deleteComment(comment);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("删除成功");
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：评论——已登录
     *
     * @param nhTopicComment
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */

    @RequestMapping(value = "/st/neighborhood/topicComment", produces = {"application/json"}, method = RequestMethod.POST)
    public String topicComment(LjhNeighborTopicComment nhTopicComment,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        IRequest requestContext = createRequestContext(request);
        //检验ID
        if (StringUtils.isBlank(nhTopicComment.getTopicId())) {
            RetApp retApp = new RetApp(FAIL, "话题ID为空!", "");
            return renderString(response, retApp);
        }
        //检验评论内容
        if (StringUtils.isBlank(nhTopicComment.getContent())) {
            RetApp retApp = new RetApp(FAIL, "评论内容为空!", "");
            return renderString(response, retApp);
        }
        //获取token
        String token = nhTopicComment.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        //评论
        nhTopicComment.setId(IdGen.uuid());
        nhTopicComment.setUserId(userId);
        JSONObject jsonObject = nhTopicCommentService.publishComment(requestContext, nhTopicComment);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("评论成功");
        JSONObject integral = new JSONObject();
        integral.put("integralResult", jsonObject);
        retApp.setData(integral);
        return renderString(response, retApp);
    }

    /**
     * 回复评论
     *
     * @param nhTopicReply
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/neighborhood/topicCommentReply", produces = {"application/json"}, method = RequestMethod.POST)
    public String topicCommentReply(LjhNeighborTopicReply nhTopicReply,
                                    HttpServletRequest request, HttpServletResponse response) {
        //接口返回值整理
        RetApp retApp = new RetApp();
        IRequest requestContext = createRequestContext(request);
        if (StringUtils.isBlank(nhTopicReply.getCommentId())) {
            retApp = new RetApp(FAIL, "评论ID为空!", "");
            return renderString(response, retApp);
        }
        if (StringUtils.isBlank(nhTopicReply.getReplyId())) {
            retApp = new RetApp(FAIL, "回复记录ID为空!", "");
            return renderString(response, retApp);
        }
        if (StringUtils.isBlank(nhTopicReply.getContent())) {
            retApp = new RetApp(FAIL, "评论内容为空!", "");
            return renderString(response, retApp);
        }
        //获取token
        String token = nhTopicReply.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        //回复评论
        JSONObject obj = new JSONObject();
        nhTopicReply.setId(IdGen.uuid());
        nhTopicReply.setUserId(userId);
        List<LjhNeighborTopicReply> replyList  = nhTopicReplyService.publishReply(requestContext, nhTopicReply);

        retApp.setData(replyList);
        retApp.setStatus(OK);
        retApp.setMessage("回复成功");
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：评论列表——公用（分页）
     *
     * @param page
     * @param nhTopicCommentView
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/neighborhood/topicCommentList", produces = {"application/json"}, method = RequestMethod.POST)
    public String topicCommentList(AppPage<NhTopicCommentView> page, NhTopicCommentView nhTopicCommentView,
                                   HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        //检验ID
        if (StringUtils.isBlank(nhTopicCommentView.getTopicId())) {
            RetApp retApp = new RetApp(FAIL, "话题ID为空!", "");
            return renderString(response, retApp);
        }
        //查询评论信息
        AppPage<NhTopicCommentView> comments = nhTopicCommentViewService.findPage(requestContext, page, nhTopicCommentView);
        comments = getCommentPath(comments);
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("查询成功");
        retApp.setData(comments);
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：点赞——已登录
     *
     * @param nhTopicPraise
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/st/neighborhood/topicPraise", produces = {"application/json"}, method = RequestMethod.POST)
    public String topicPraise(LjhNeighborTopicPraise nhTopicPraise,
                              HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        //检验ID
        if (StringUtils.isBlank(nhTopicPraise.getTopicId())) {
            RetApp retApp = new RetApp(FAIL, "话题ID为空!", "");
            return renderString(response, retApp);
        }
        //获取token
        String token = nhTopicPraise.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        //判断当前点赞状态
        nhTopicPraise.setId(IdGen.uuid());
        nhTopicPraise.setUserId(userId);
        List<LjhNeighborTopicPraise> nhTopicPraises = nhTopicPraiseService.findList(requestContext, nhTopicPraise);
        if (nhTopicPraises == null || nhTopicPraises.size() == 0) {
            //点赞
            nhTopicPraiseService.insertSelective(requestContext, nhTopicPraise);
        } else {
            RetApp retApp = new RetApp(FAIL, "已点赞，请勿重复点赞", "");
            renderString(response, retApp);
        }
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("点赞成功");
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：取消点赞——已登录
     *
     * @param nhTopicPraise
     * @param request
     * @param response
     * @return String
     * @author:jinghao.che@zymobi.com
     */
    @RequestMapping(value = "/st/neighborhood/topicOffPraise", produces = {"application/json"}, method = RequestMethod.POST)
    public String topicOffPraise(LjhNeighborTopicPraise nhTopicPraise,
                                 HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        //检验ID
        if (StringUtils.isBlank(nhTopicPraise.getTopicId())) {
            RetApp retApp = new RetApp(FAIL, "话题ID为空!", "");
            return renderString(response, retApp);
        }
        //获取token
        String token = nhTopicPraise.getToken();
        //根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        //判断当前点赞状态
        nhTopicPraise.setUserId(userId);
        List<LjhNeighborTopicPraise> nhTopicPraises = nhTopicPraiseService.findList(requestContext, nhTopicPraise);
        if (nhTopicPraises != null && nhTopicPraises.size() > 0) {
            //取消点赞
            for (LjhNeighborTopicPraise praise : nhTopicPraises) {
                praise.setDelFlag("1");
                nhTopicPraiseService.updateByPrimaryKeySelective(requestContext, praise);
            }
        }
        //接口返回值整理
        RetApp retApp = new RetApp();
        retApp.setStatus(OK);
        retApp.setMessage("取消点赞成功");
        return renderString(response, retApp);
    }

    /**
     * 此方法描述的是：获取话题信息中图片信息及处理当前点赞状态
     *
     * @param nhTopicViews
     * @param userId
     * @return Page<NhTopicView>
     * @author:jinghao.che@zymobi.com
     */
    public AppPage<NhTopicView> getNhTopicImages(AppPage<NhTopicView> nhTopicViews, String userId) {

        List<NhTopicView> nhTopicViewList;
        if (nhTopicViews != null && (nhTopicViewList = nhTopicViews.getList()) != null && nhTopicViewList.size() > 0) {
            //查询话题图片信息
            List<LjhNeighborTopicImage> images = nhTopicImageService.findListByTopicList(nhTopicViewList);
            Map<String, List<LjhNeighborTopicImage>> map = new HashMap<>();
            if (null != images && images.size() > 0) {
                for (LjhNeighborTopicImage image : images) {
                    image.setUrl(Global.getFullImagePath(image.getUrl()));
                    image.setBreviaryUrl(Global.getFullImagePath(image.getUrl()));
                    if (map.containsKey(image.getTopicId())) {
                        List<LjhNeighborTopicImage> list = map.get(image.getTopicId());
                        list.add(image);
                    } else {
                        List<LjhNeighborTopicImage> list = new ArrayList<>();
                        list.add(image);
                        map.put(image.getTopicId(), list);
                    }
                }
            }

            Map<String, Object> praiseMap = null;
            if (!StringUtils.isEmpty(userId)) {
                List<LjhNeighborTopicPraise> nhTopicPraises = nhTopicPraiseService.findListByTopicList(userId, nhTopicViewList);
                if (null != nhTopicPraises && nhTopicPraises.size() > 0) {
                    praiseMap = new HashMap();
                    for (LjhNeighborTopicPraise praise : nhTopicPraises) {
                        praiseMap.put(praise.getTopicId(), praise.getId());
                    }
                }
            }

            for (int i = 0; i < nhTopicViewList.size(); i++) {
                NhTopicView entry = nhTopicViewList.get(i);
                if (map.containsKey(entry.getId())) {
                    entry.setMedias(map.get(entry.getId()));
                }
                //处理头像路径
                entry.setUserIcon(Global.getFullImagePathForReduce(entry.getUserIcon()));
                //是否已点赞
                if (null != praiseMap && praiseMap.containsKey(entry.getId())) {
                    entry.setPraiseFlag(true);
                } else {
                    entry.setPraiseFlag(false);
                }
            }
        }
        return nhTopicViews;
    }

    /**
     * 此方法描述的是：评论头像处理
     *
     * @param comments
     * @return AppPage<NhTopicCommentView>
     * @author:jinghao.che@zymobi.com
     */
    public AppPage<NhTopicCommentView> getCommentPath(AppPage<NhTopicCommentView> comments) {
        if (comments != null && comments.getList() != null && comments.getList().size() > 0) {
            for (int i = 0; i < comments.getList().size(); i++) {
                NhTopicCommentView nhTopicCommentView = comments.getList().get(i);
                if (nhTopicCommentView != null) {
                    //处理图片路径
                    nhTopicCommentView.setUserIcon(Global.getFullImagePath(nhTopicCommentView.getUserIcon()));
                }
            }
        }
        return comments;
    }

    /**
     * 此方法描述的是：处理图片路径
     *
     * @param images
     * @return List<NhTopicImage>
     * @author:jinghao.che@zymobi.com
     */
    public List<LjhNeighborTopicImage> getImageServerPath(List<LjhNeighborTopicImage> images) {
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                LjhNeighborTopicImage image = images.get(i);
                if (image != null) {
                    //缩略图路径
                    //images.get(i).setBreviaryUrl(imagePath+"reduce/"+images.get(i).getUrl());
                    image.setBreviaryUrl(Global.getFullImagePath(image.getUrl()));
                    //处理图片路径
                    image.setUrl(Global.getFullImagePath(image.getUrl()));
                }
            }
        }
        return images;
    }

}
