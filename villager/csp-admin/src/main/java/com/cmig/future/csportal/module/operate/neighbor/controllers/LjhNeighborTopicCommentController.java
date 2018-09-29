package com.cmig.future.csportal.module.operate.neighbor.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopicComment;
import com.cmig.future.csportal.module.operate.neighbor.service.ILjhNeighborTopicCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class LjhNeighborTopicCommentController extends BaseController{

    @Autowired
    private ILjhNeighborTopicCommentService service;


    @RequestMapping(value = "/csp/ljh/neighbor/topic/comment/query")
    @ResponseBody
    public ResponseData query(LjhNeighborTopicComment dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/neighbor/topic/comment/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<LjhNeighborTopicComment> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/neighbor/topic/comment/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LjhNeighborTopicComment> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }