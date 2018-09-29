package com.cmig.future.csportal.module.operate.articleNotify.controllers;

import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.operate.articleNotify.dto.ArticleNotify;
import com.cmig.future.csportal.module.operate.articleNotify.service.IArticleNotifyService;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserCommunityService;
import com.cmig.future.csportal.module.sys.notifyrecord.service.ISysNotifyRecordService;
import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.cmig.future.csportal.module.sys.sysattachment.service.ISysAttachmentService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
    public class ArticleNotifyController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ArticleNotifyController.class);
    @Autowired
    private IArticleNotifyService articleNotifyService;

    @Autowired
    private IMgtUserCommunityService mgtUserCommunityService;

    @Autowired
    private ISysNotifyRecordService sysNotifyRecordService;

    @Autowired
    private ISysAttachmentService sysAttachmentService;

    @RequestMapping(value = "/articleNotify/query")
    @ResponseBody
    public ResponseData query(ArticleNotify dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(articleNotifyService.selectArticleNotify(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/articleNotify/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<ArticleNotify> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(articleNotifyService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/articleNotify/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ArticleNotify> dto){
        articleNotifyService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/articleNotify/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,@RequestBody List<ArticleNotify> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(articleNotifyService.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/articleNotify/queryArticleNotifyById")
    @ResponseBody
    public ResponseData queryArticleNotifyById(HttpServletRequest request,@RequestParam(required = true)String id){
        IRequest requestCtx = createRequestContext(request);
        Long userId = requestCtx.getUserId();
        ArticleNotify articleNotify = articleNotifyService.queryArticleNotifyById(id);
        return new ResponseExtData(articleNotify);
    }

    @RequestMapping(value = "/articleNotify/deleteArticleNotifyById")
    @ResponseBody
    public ResponseData deleteArticleNotifyById(HttpServletRequest request,@RequestParam(required = true)String id){
        IRequest requestCtx = createRequestContext(request);
        ArticleNotify articleNotify = articleNotifyService.queryArticleNotifyById(id);
        articleNotify.setLastUpdatedBy(requestCtx.getUserId());
        articleNotifyService.deleteArticleNotifyById(articleNotify);
        return new ResponseData();
    }

    @RequestMapping(value = "/articleNotify/publishForCommunity")
    @ResponseBody
    public ResponseData publishForCommunity(HttpServletRequest request, MgtUserCommunity mgtUserCommunity,
                                            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize){

        IRequest requestCtx = createRequestContext(request);
        List<MgtUserCommunity> list = mgtUserCommunityService.findPublishCommunityList(requestCtx,mgtUserCommunity,page,pageSize);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/articleNotify/publishForUser")
    @ResponseBody
    public ResponseData publishForUser(HttpServletRequest request, MgtUserCommunity mgtUserCommunity,
                                            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize){

        IRequest requestCtx = createRequestContext(request);
        List<MgtUserCommunity> list = mgtUserCommunityService.findPublishUserList(requestCtx,mgtUserCommunity,page,pageSize);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/articleNotify/publishToCommunity")
    @ResponseBody
    public ResponseData publishToCommunity(HttpServletRequest request, @RequestParam(required = true)String articleId,
                                           @RequestParam(required = true)String selectIds){

        IRequest requestCtx = createRequestContext(request);
        ArticleNotify articleNotify = articleNotifyService.queryArticleNotifyById(articleId);
        String content = articleNotify.getTitle();
        sysNotifyRecordService.publishToCommunity(requestCtx,articleNotify.getId(),content,selectIds);
        articleNotify.setStatus(Constants.CMS_NOTIFY_PUBLISHED);
        List<ArticleNotify> dto = new ArrayList<>();
        dto.add(articleNotify);

        return new ResponseData(articleNotifyService.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/articleNotify/publishToUser")
    @ResponseBody
    public ResponseData publishToUser(HttpServletRequest request, @RequestParam(required = true)String articleId,
                                           @RequestParam(required = true)String selectUserIds){

        IRequest requestCtx = createRequestContext(request);
        ArticleNotify articleNotify = articleNotifyService.queryArticleNotifyById(articleId);
        String content = articleNotify.getTitle();
        sysNotifyRecordService.publishToUser(requestCtx,articleNotify.getId(),content,selectUserIds);
        articleNotify.setStatus(Constants.CMS_NOTIFY_PUBLISHED);
        List<ArticleNotify> dto = new ArrayList<>();
        dto.add(articleNotify);

        return new ResponseData(articleNotifyService.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/articleNotify/querySysAttachmentByObjectId")
    @ResponseBody
    public ResponseData querySysAttachmentByObjectId(HttpServletRequest request,SysAttachment sysAttachment){
        List<SysAttachment> list = sysAttachmentService.findList(sysAttachment);
        return new ResponseData(list);
    }
    }