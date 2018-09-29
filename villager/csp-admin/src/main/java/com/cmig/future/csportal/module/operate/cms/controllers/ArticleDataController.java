package com.cmig.future.csportal.module.operate.cms.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.module.operate.cms.dto.ArticleData;
import com.cmig.future.csportal.module.operate.cms.service.IArticleDataService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

    @Controller
    public class ArticleDataController extends BaseController{

    @Autowired
    private IArticleDataService service;


    @RequestMapping(value = "/cms/article/data/query")
    @ResponseBody
    public ResponseData query(ArticleData dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/cms/article/data/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<ArticleData> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/cms/article/data/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<ArticleData> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }