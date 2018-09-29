package com.cmig.future.csportal.module.properties.base.contacter.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.properties.base.contacter.dto.BpOwnerContacter;
import com.cmig.future.csportal.module.properties.base.contacter.service.IBpOwnerContacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class BpOwnerContacterController extends BaseController{

    @Autowired
    private IBpOwnerContacterService service;


    @RequestMapping(value = "/csp/bp/owner/contacter/query")
    @ResponseBody
    public ResponseData query(BpOwnerContacter dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/bp/owner/contacter/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<BpOwnerContacter> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/bp/owner/contacter/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<BpOwnerContacter> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }