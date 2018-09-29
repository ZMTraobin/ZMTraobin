package com.cmig.future.csportal.module.properties.base.customer.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmig.future.csportal.module.properties.base.customer.CustomerInfoException;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwner;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpOwnerService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

    @Controller
    public class BpOwnerController extends BaseController{

    @Autowired
    private IBpOwnerService service;


    @RequestMapping(value = "/csp/bp/owner/query")
    @ResponseBody
    public ResponseData query(BpOwner dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/csp/bp/owner/queryOwners")
    @ResponseBody
    public ResponseData queryOwners(BpOwner dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryOwners(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/bp/owner/save")
    @ResponseBody
    public ResponseData save(HttpServletRequest request,@RequestBody List<BpOwner> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }
    
    @RequestMapping(value = "/csp/bp/owner/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<BpOwner> dto) throws CustomerInfoException{
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/bp/owner/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<BpOwner> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/bp/owner/findGeneralByBuildingId")
    @ResponseBody
    public ResponseData findGeneralByBuildingId(HttpServletRequest request,Long buildingId){
        List<BpOwner> dto = service.findGeneralByBuildingId(buildingId);
        return new ResponseData(dto);
    }
    
    @RequestMapping(value = "/csp/bp/owner/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request,@RequestBody List<BpOwner> dto) throws CustomerInfoException{
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.saveOrUpdate(requestCtx, dto));
    }
    
    /**
     * 删除业主（1.删除业主记录，2.删除业主-房屋关系表）
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "/csp/bp/owner/delete")
    @ResponseBody
    public ResponseData deleteOwner(HttpServletRequest request,@RequestBody List<BpOwner> dto){
        service.deleteOwner(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/csp/bp/owner/queryInfoById")
    @ResponseBody
    public ResponseData query(@RequestParam(required=true) String id, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectCustomer(requestContext,Long.valueOf(id),page,pageSize));
    }

    }