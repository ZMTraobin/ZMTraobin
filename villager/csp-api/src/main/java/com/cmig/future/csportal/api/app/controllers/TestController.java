package com.cmig.future.csportal.api.app.controllers;

import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.verify.RSAUtilsWithKey;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Controller
@ResponseBody
public class TestController extends BaseExtendController {

    @Autowired
    private IAppUserService appUserService;

    @RequestMapping(value = "/api/test", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp test1(@ModelAttribute AppUser data, HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        JedisUtils.set("zhangtao","1",0);
        logger.info(JedisUtils.get("zhangtao"));
        retApp.setStatus("222");
        retApp.setMessage("修改用户成功!");
        return retApp;
    }

    @RequestMapping(value = "/i/api/test", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp test2(@ModelAttribute AppUser data, HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        JedisUtils.set("zhangtao","1",0);
        logger.info(JedisUtils.get("zhangtao"));
        retApp.setStatus("1111");
        retApp.setMessage("修改用户成功!");
        return retApp;
    }

    @RequestMapping(value = "/r/api/test", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp test3(@ModelAttribute AppUser data, HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        JedisUtils.set("zhangtao","1",0);
        logger.info(JedisUtils.get("zhangtao"));
        retApp.setStatus("222333");
        retApp.setMessage("修改用户成功!");
        return retApp;
    }

    @RequestMapping(value = "/i/api/test2", produces = {"application/json"}, method = RequestMethod.POST)
    public String test22(@ModelAttribute AppUser data, HttpServletRequest request, HttpServletResponse response) {
        RetApp retApp = new RetApp();
        JedisUtils.set("zhangtao","1",0);
        logger.info(JedisUtils.get("zhangtao"));
        retApp.setStatus("222334443111");
        retApp.setMessage("修改用户成功!");
        return JSONObject.fromObject(retApp).toString();
    }

    @RequestMapping(value = "/i/api//test4", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp test2222(@ModelAttribute AppUser data, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RetApp retApp = new RetApp();
	    String content = getParam(request,"content","");
	    String temp2=RSAUtilsWithKey.encrypt(content);
	    logger.debug(temp2);
	    logger.debug(RSAUtilsWithKey.decrypt(temp2));
	    retApp.setData(URLEncoder.encode(temp2, "UTF-8"));
        retApp.setStatus(OK);
        retApp.setMessage("");
        return retApp;
    }
}
