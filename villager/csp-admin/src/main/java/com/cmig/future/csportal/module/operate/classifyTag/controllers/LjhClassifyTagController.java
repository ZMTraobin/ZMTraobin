package com.cmig.future.csportal.module.operate.classifyTag.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.operate.classifyTag.dto.LjhClassifyTag;
import com.cmig.future.csportal.module.operate.classifyTag.service.ILjhClassifyTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class LjhClassifyTagController extends BaseController{

    @Autowired
    private ILjhClassifyTagService service;


    @RequestMapping(value = "/csp/ljh/classify/tag/query")
    @ResponseBody
    public ResponseData query(LjhClassifyTag dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

//    @RequestMapping(value = "/csp/ljh/classify/tag/submit")
//    @ResponseBody
//    public ResponseData update(HttpServletRequest request,@RequestBody List<LjhClassifyTag> dto){
//        IRequest requestCtx = createRequestContext(request);
//        return new ResponseData(service.batchUpdate(requestCtx, dto));
//    }
    
    @RequestMapping(value = "/csp/ljh/classify/tag/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<LjhClassifyTag> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/classify/tag/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LjhClassifyTag> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }