package com.cmig.future.csportal.module.villager.wealth.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.villager.wealth.dto.PlantCost;
import com.cmig.future.csportal.module.villager.wealth.service.IPlantCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class PlantCostController extends BaseController{

    @Autowired
    private IPlantCostService service;


    @RequestMapping(value = "/xl/plant/cost/query")
    @ResponseBody
    public ResponseData query(PlantCost dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/xl/plant/cost/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<PlantCost> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/xl/plant/cost/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<PlantCost> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }