package com.cmig.future.csportal.module.villager.integral.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralDetail;
import com.cmig.future.csportal.module.villager.integral.service.IIntegralDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class IntegralDetailController extends BaseController{

    @Autowired
    private IIntegralDetailService service;


    @RequestMapping(value = "/xl/integral/detail/query")
    @ResponseBody
    public ResponseData query(IntegralDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/xl/integral/detail/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<IntegralDetail> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/xl/integral/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<IntegralDetail> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }