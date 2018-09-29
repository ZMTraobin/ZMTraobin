package com.cmig.future.csportal.module.properties.base.customer.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwner;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpHouseMapService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

    @Controller
    public class BpHouseMapController extends BaseController{

    @Autowired
    private IBpHouseMapService service;


    @RequestMapping(value = "/csp/bp/house/map/query")
    @ResponseBody
    public ResponseData query(BpHouseMap dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/csp/bp/house/map/queryByBpId")
    @ResponseBody
    public ResponseData queryByBpId(BpOwner bpOwner, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryByBpId(requestContext,bpOwner.getBpId(),page,pageSize));
    }

    @RequestMapping(value = "/csp/bp/house/map/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<BpHouseMap> dto){
        IRequest requestCtx = createRequestContext(request);
        //return new ResponseData(service.batchUpdate(requestCtx, dto));
        return new ResponseData(service.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/bp/house/map/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<BpHouseMap> dto){
        //service.batchDelete(dto);
        service.batchValid(dto);
        return new ResponseData();
    }
    }