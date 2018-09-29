package com.cmig.future.csportal.module.properties.base.customer.controllers;

import com.cmig.future.csportal.module.properties.base.customer.dto.BpGeneral;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpGeneralService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class BpGeneralController extends BaseController{

    @Autowired
    private IBpGeneralService service;


    @RequestMapping(value = "/csp/bp/general/query")
    @ResponseBody
    public ResponseData query(BpGeneral dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }
    
    @RequestMapping(value = "/csp/bp/general/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<BpGeneral> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/bp/general/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<BpGeneral> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }