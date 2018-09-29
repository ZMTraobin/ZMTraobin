package com.cmig.future.csportal.module.properties.communityEvent.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.properties.communityEvent.dto.LjhEventCommunity;
import com.cmig.future.csportal.module.properties.communityEvent.service.ILjhEventCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class LjhEventCommunityController extends BaseController{

    @Autowired
    private ILjhEventCommunityService service;

    @RequestMapping(value = "/csp/ljh/event/community/query")
    @ResponseBody
    public ResponseData query(LjhEventCommunity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/csp/ljh/event/community/queryByEvent")
    @ResponseBody
    public ResponseData queryByEvent(LjhEventCommunity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByEvent(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/event/community/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<LjhEventCommunity> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.submit(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/event/community/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LjhEventCommunity> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }