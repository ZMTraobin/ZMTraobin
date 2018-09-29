package com.cmig.future.csportal.module.kpi.controllers;

import com.cmig.future.csportal.module.kpi.dto.LjhKpiDuration;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiDurationService;
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
public class LjhKpiDurationController extends BaseController{

@Autowired
private ILjhKpiDurationService service;


@RequestMapping(value = "/csp/ljh/kpi/duration/query")
@ResponseBody
public ResponseData query(LjhKpiDuration dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    IRequest requestContext = createRequestContext(request);
    return new ResponseData(service.select(requestContext,dto,page,pageSize));
}

@RequestMapping(value = "/csp/ljh/kpi/duration/submit")
@ResponseBody
public ResponseData update(HttpServletRequest request,@RequestBody List<LjhKpiDuration> dto){
    IRequest requestCtx = createRequestContext(request);
    return new ResponseData(service.batchUpdate(requestCtx, dto));
}

@RequestMapping(value = "/csp/ljh/kpi/duration/remove")
@ResponseBody
public ResponseData delete(HttpServletRequest request,@RequestBody List<LjhKpiDuration> dto){
    service.batchDelete(dto);
    return new ResponseData();
}
}