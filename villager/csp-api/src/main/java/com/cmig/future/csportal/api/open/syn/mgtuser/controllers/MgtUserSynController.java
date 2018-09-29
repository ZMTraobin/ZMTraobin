package com.cmig.future.csportal.api.open.syn.mgtuser.controllers;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "${commonPath}/sys/user")
    public class MgtUserSynController extends BaseExtendController{

	@Autowired
	private IMgtUserService mgtUserService;

    @RequestMapping(value = "/addUser", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp addUser(@ModelAttribute
                          MgtUser mgtUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    mgtUserService.synMgtUserAdd(mgtUser, request);
        return RetAppUtil.success("新增成功");
    }

	/**
     * 物业员工修改
     * @param mgtUser
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateUser", produces = { "application/json" }, method = RequestMethod.POST)
    public RetApp updateUser(@ModelAttribute MgtUser mgtUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    mgtUserService.synMgtUserUpdate(mgtUser, request);
        return RetAppUtil.success("修改成功");
    }
 }