package com.cmig.future.csportal.module.sys.logs.controllers;

import com.cmig.future.csportal.module.sys.logs.dto.LjhLoginLog;
import com.cmig.future.csportal.module.sys.logs.service.ILjhLoginLogService;
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
public class LjhLoginLogController extends BaseController{

@Autowired
private ILjhLoginLogService service;


@RequestMapping(value = "/csp/ljh/login/log/query")
@ResponseBody
public ResponseData query(LjhLoginLog dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
    IRequest requestContext = createRequestContext(request);
    return new ResponseData(service.select(requestContext,dto,page,pageSize));
}

@RequestMapping(value = "/csp/ljh/login/log/submit")
@ResponseBody
public ResponseData update(HttpServletRequest request,@RequestBody List<LjhLoginLog> dto){
    IRequest requestCtx = createRequestContext(request);
    return new ResponseData(service.batchUpdate(requestCtx, dto));
}

@RequestMapping(value = "/csp/ljh/login/log/remove")
@ResponseBody
public ResponseData delete(HttpServletRequest request,@RequestBody List<LjhLoginLog> dto){
    service.batchDelete(dto);
    return new ResponseData();
}
}