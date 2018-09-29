package com.cmig.future.csportal.module.user.appuser.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserAuth;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

    @Controller
    public class AppUserAuthController extends BaseController{

    @Autowired
    private IAppUserAuthService service;


    @RequestMapping(value = "/csp/ljh/app/user/auth/query")
    @ResponseBody
    public ResponseData query(AppUserAuth dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.select(requestContext,dto,page,pageSize));
    }

    @RequestMapping(value = "/csp/ljh/app/user/auth/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request,@RequestBody List<AppUserAuth> dto){
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/app/user/auth/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request,@RequestBody List<AppUserAuth> dto){
        service.batchDelete(dto);
        return new ResponseData();
    }
    }