package com.cmig.future.csportal.module.pay.refund.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.pay.refund.dto.OrderFormRefund;
import com.cmig.future.csportal.module.pay.refund.service.IOrderFormRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class OrderFormRefundController extends BaseController{

    @Autowired
    private IOrderFormRefundService service;


    @RequestMapping(value = "/csp/order/form/refund/query")
    @ResponseBody
    public ResponseData query(OrderFormRefund dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/order/form/refund/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<OrderFormRefund> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/order/form/refund/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<OrderFormRefund> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/csp/order/form/refund/queryByOrder")
    @ResponseBody
    public ResponseData queryByOrder(OrderFormRefund dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByOrder(requestContext,dto,page,pageSize));
    }
    
    }