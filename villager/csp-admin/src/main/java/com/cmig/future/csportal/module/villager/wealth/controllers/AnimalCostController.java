package com.cmig.future.csportal.module.villager.wealth.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalCost;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class AnimalCostController extends BaseController{

    @Autowired
    private IAnimalCostService service;


    @RequestMapping(value = "/xl/animal/cost/query")
    @ResponseBody
    public ResponseData query(AnimalCost dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/xl/animal/cost/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<AnimalCost> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/xl/animal/cost/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<AnimalCost> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }