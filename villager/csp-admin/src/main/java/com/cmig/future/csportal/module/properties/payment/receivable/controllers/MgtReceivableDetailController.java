package com.cmig.future.csportal.module.properties.payment.receivable.controllers;

import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.cmig.future.csportal.module.properties.payment.receivable.service.IMgtReceivableDetailService;
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
    public class MgtReceivableDetailController extends BaseController {

    @Autowired
    private IMgtReceivableDetailService service;


    @RequestMapping(value = "/csp/mgt/receivable/detail/query")
    @ResponseBody
    public ResponseData query(MgtReceivableDetail dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryMgtReceivableDetail(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/mgt/receivable/detail/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<MgtReceivableDetail> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/mgt/receivable/detail/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<MgtReceivableDetail> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }