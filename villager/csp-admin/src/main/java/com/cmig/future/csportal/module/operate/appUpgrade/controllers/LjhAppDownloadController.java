package com.cmig.future.csportal.module.operate.appUpgrade.controllers;

import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.operate.appUpgrade.dto.LjhAppDownload;
import com.cmig.future.csportal.module.operate.appUpgrade.service.ILjhAppDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class LjhAppDownloadController extends BaseController{

    @Autowired
    private ILjhAppDownloadService service;


    @RequestMapping(value = "/csp/ljh/app/download/query")
    @ResponseBody
    public ResponseData query(LjhAppDownload dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.findList(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/csp/ljh/app/download/queryById")
    @ResponseBody
    public ResponseData queryById(LjhAppDownload dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectApp(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/app/download/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<LjhAppDownload> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
    
    @RequestMapping(value = "/csp/ljh/app/download/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,@RequestBody List<LjhAppDownload> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/app/download/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,LjhAppDownload dto){
        IRequest requestCtx = createRequestContext(request);
        dto.setDelFlag(dto.DEL_FLAG_DELETE);
        dto = service.updateByPrimaryKeySelective(requestCtx,dto);
        return new ResponseExtData(dto);
    }
    }