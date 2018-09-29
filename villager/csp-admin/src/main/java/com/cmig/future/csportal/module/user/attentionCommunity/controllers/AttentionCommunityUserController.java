package com.cmig.future.csportal.module.user.attentionCommunity.controllers;

import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class AttentionCommunityUserController extends BaseController {

    @Autowired
    private IAttentionCommunityUserService attentionCommunityUserService;


    @RequestMapping(value = "/ljh/attention/community/user/query")
    @ResponseBody
    public ResponseData query(AttentionCommunityUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(attentionCommunityUserService.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/ljh/attention/community/user/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<AttentionCommunityUser> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(attentionCommunityUserService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/ljh/attention/community/user/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<AttentionCommunityUser> dto){
        attentionCommunityUserService.batchDelete(dto);
        return new ResponseData();
    }
    }