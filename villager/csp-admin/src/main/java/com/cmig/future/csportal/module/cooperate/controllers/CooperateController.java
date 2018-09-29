package com.cmig.future.csportal.module.cooperate.controllers;

import com.cmig.future.csportal.module.cooperate.dto.Cooperate;
import com.cmig.future.csportal.module.cooperate.service.ICooperateService;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class CooperateController extends BaseController{

    @Autowired
    private ICooperateService cooperateService;


    @RequestMapping(value = "/csp/ljh/cooperate/query")
    @ResponseBody
    public ResponseData query(Cooperate dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(cooperateService.selectAllCooperate(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/cooperate/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<Cooperate> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(cooperateService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/cooperate/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<Cooperate> dto){
        cooperateService.batchDelete(dto);
        return new ResponseData();
    }
    }