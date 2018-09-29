package com.cmig.future.csportal.module.user.appuser.controllers;

import com.cmig.future.csportal.module.base.entity.ResponseExtData;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.dto.AttentionCommunityUser;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
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
public class AppUserWebController extends BaseController {

    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private IAttentionCommunityUserService attentionCommunityUserService;


    @RequestMapping(value = "/ljh/app/user/query")
    @ResponseBody
    public ResponseData query(AppUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(appUserService.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/ljh/app/user/get")
    @ResponseBody
    public ResponseData get(AppUser dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        dto=appUserService.selectByPrimaryKey(requestContext,dto);
        return new ResponseExtData(dto);
    }

    @RequestMapping(value = "/ljh/app/user/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<AppUser> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(appUserService.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/ljh/app/user/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<AppUser> dto) {
        appUserService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/ljh/app/user/getCommunityList")
    @ResponseBody
    public ResponseData getCommunityList(HttpServletRequest request, AppUser dto) {

        IRequest requestContext = createRequestContext(request);
        String userId = dto.getId();
        String communityName = dto.getCommunityName();
        AttentionCommunityUser data = new AttentionCommunityUser();
        data.setUserId(userId);
        data.setCommunityName(communityName);
        List<AttentionCommunityUser> list = attentionCommunityUserService.getCommunityList(requestContext,data,1,10);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/ljh/app/user/findList")
    @ResponseBody
    public ResponseData findList(AppUser dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<AppUser> list = appUserService.findList(requestContext, dto, page, pageSize);
        return new ResponseData(list);
    }

    @RequestMapping(value = "/ljh/app/user/appUserList")
    @ResponseBody
    public ResponseData appUserList(AppUser dto, HttpServletRequest request) {
        List<AppUser> list = appUserService.appUserList(dto);
        return new ResponseData(list);
    }
}