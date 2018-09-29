package com.cmig.future.csportal.module.properties.authorManager.controllers;

import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.account.dto.User;
import com.hand.hap.account.service.IUserService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import com.cmig.future.csportal.module.properties.authorManager.dto.LjhSysUserCommunity;
import com.cmig.future.csportal.module.properties.authorManager.service.ILjhSysUserCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LjhSysUserCommunityController extends BaseController {

    @Autowired
    private ILjhSysUserCommunityService service;
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/csp/ljh/sys/user/community/query")
    @ResponseBody
    public ResponseData query(LjhSysUserCommunity dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.selectByUser(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/csp/ljh/sys/user/community/queryCommunitiesNoAuth")
    @ResponseBody
    public ResponseData queryCommunitiesNoAuth(LjhSysUserCommunity dto,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(service.queryCommunitiesNoAuth(requestContext, dto, page, pageSize));
    }

    // @RequestMapping(value = "/csp/ljh/sys/user/community/submit")
    // @ResponseBody
    // public ResponseData update(HttpServletRequest request,@RequestBody
    // List<LjhSysUserCommunity> dto){
    // IRequest requestCtx = createRequestContext(request);
    // return new ResponseData(service.batchUpdate(requestCtx, dto));
    // }
    @RequestMapping(value = "/csp/ljh/sys/user/community/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<LjhSysUserCommunity> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.saveUserCommunity(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/ljh/sys/user/community/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<LjhSysUserCommunity> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/csp/ljh/sys/user/query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData selectUsers(HttpServletRequest request,@ModelAttribute User user, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize) {
        IRequest iRequest = createRequestContext(request);
        return new ResponseData(userService.select(iRequest, user, page, pagesize));
    }
}