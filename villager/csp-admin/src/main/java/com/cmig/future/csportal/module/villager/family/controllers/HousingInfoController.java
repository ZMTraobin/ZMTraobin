package com.cmig.future.csportal.module.villager.family.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.villager.family.dto.HousingInfo;
import com.cmig.future.csportal.module.villager.family.service.IHousingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class HousingInfoController extends BaseController{

    @Autowired
    private IHousingInfoService service;


    @RequestMapping(value = "/xl/housing/info/query")
    @ResponseBody
    public ResponseData query(HousingInfo dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/xl/housing/info/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<HousingInfo> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/xl/housing/info/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<HousingInfo> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }