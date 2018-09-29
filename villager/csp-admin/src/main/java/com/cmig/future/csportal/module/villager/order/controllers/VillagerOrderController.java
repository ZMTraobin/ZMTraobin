package com.cmig.future.csportal.module.villager.order.controllers;

import com.cmig.future.csportal.module.villager.order.dto.VillagerOrder;
import com.cmig.future.csportal.module.villager.order.service.IVillagerOrderService;
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
public class VillagerOrderController extends BaseController {

    @Autowired
    private IVillagerOrderService service;


    @RequestMapping(value = "/csp/villager/order/query")
    @ResponseBody
    public ResponseData query(VillagerOrder dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.findList(dto, page, pageSize));
    }

    @RequestMapping(value = "/csp/villager/order/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<VillagerOrder> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/villager/order/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<VillagerOrder> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/villager/order/send")
    @ResponseBody
    public ResponseData send(HttpServletRequest request, @RequestBody List<VillagerOrder> dto) {
        service.batchSend(dto);
        return new ResponseData();
    }
}