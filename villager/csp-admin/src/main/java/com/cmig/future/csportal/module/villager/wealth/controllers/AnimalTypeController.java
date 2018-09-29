package com.cmig.future.csportal.module.villager.wealth.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.villager.wealth.dto.AnimalType;
import com.cmig.future.csportal.module.villager.wealth.service.IAnimalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class AnimalTypeController extends BaseController{

    @Autowired
    private IAnimalTypeService service;


    @RequestMapping(value = "/xl/animal/type/query")
    @ResponseBody
    public ResponseData query(AnimalType dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/xl/animal/type/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<AnimalType> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/xl/animal/type/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<AnimalType> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }