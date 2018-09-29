package com.cmig.future.csportal.module.lifepay.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.lifepay.dto.LifePayBill;
import com.cmig.future.csportal.module.lifepay.service.ILifePayBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class LifePayBillController extends BaseController{

    @Autowired
    private ILifePayBillService service;


    @RequestMapping(value = "/csp/life/pay/bill/query")
    @ResponseBody
    public ResponseData query(LifePayBill dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/life/pay/bill/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<LifePayBill> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/life/pay/bill/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<LifePayBill> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }