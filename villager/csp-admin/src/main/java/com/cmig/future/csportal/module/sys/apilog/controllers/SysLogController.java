package com.cmig.future.csportal.module.sys.apilog.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.sys.apilog.dto.SysLog;
import com.cmig.future.csportal.module.sys.apilog.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SysLogController extends BaseController {

    @Autowired
    private ISysLogService service;


    @RequestMapping(value = "/ljh/sys/log/query")
    @ResponseBody
    public ResponseData query(SysLog dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/ljh/sys/log/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<SysLog> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/ljh/sys/log/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<SysLog> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}