package com.cmig.future.csportal.module.pay.order.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.module.pay.order.dto.OrderFormNotifyMc;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormNotifyMcService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

@Controller
public class OrderFormNotifyMcController extends BaseController {

    @Autowired
    private IOrderFormNotifyMcService service;

    // @RequestMapping(value = "/csp/order/form/notify/mc/query")
    // @ResponseBody
    // public ResponseData query(OrderFormNotifyMc dto,
    // @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    // @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
    // HttpServletRequest request) {
    // IRequest requestContext = createRequestContext(request);
    // return new
    // ResponseData(service.select(requestContext,dto,page,pageSize));
    // }

    @RequestMapping(value = "/csp/order/form/notify/mc/query")
    @ResponseBody
    public ResponseData query(OrderFormNotifyMc dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectOrderNotify(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/csp/order/form/notify/mc/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<OrderFormNotifyMc> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/order/form/notify/mc/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OrderFormNotifyMc> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}