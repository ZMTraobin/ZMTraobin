package com.cmig.future.csportal.module.villager.family.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.villager.family.dto.LandInfo;
import com.cmig.future.csportal.module.villager.family.service.ILandInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class LandInfoController extends BaseController{

    @Autowired
    private ILandInfoService service;


    @RequestMapping(value = "/xl/land/info/query")
    @ResponseBody
    public ResponseData query(LandInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/xl/land/info/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<LandInfo> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/xl/land/info/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LandInfo> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }