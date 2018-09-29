/**
 * .
 */
package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * ping controller
 */
@Controller
@ResponseBody
public class AppPingController extends BaseExtendController{

    private Logger logger= LoggerFactory.getLogger(AppPingController.class);

    @RequestMapping(value = "${commonPath}/ping/test")
    public RetApp access_token_owner(HttpServletRequest request) {
	    return RetAppUtil.success("1","ok");
    }

}
