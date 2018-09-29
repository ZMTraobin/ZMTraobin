package com.cmig.future.csportal.api.app.properties.controllers;

import com.cmig.future.csportal.common.utils.SmsUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpHouseMapService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${userPath}")
public class HouseIdentifyController extends BaseExtendController {

    @Autowired
    private IBpHouseMapService bpHouseMapService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/house/identify",produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp allTopicList(BpHouseMap houseMap, HttpServletRequest request, HttpServletResponse response) {
        RetApp result = new RetApp();
        String token = houseMap.getToken();
        // 根据token获取userId
        String userId = UserTokenUtils.getUserIdByToken(token);
        houseMap.setAppUserId(userId);
        Long buildingId = houseMap.getBuildingId();
        String mobile = houseMap.getMobile();
        String verifyCode = houseMap.getVerifyCode();
        if (buildingId == null) {
            RetApp retApp = new RetApp(FAIL, "房屋id不能为空", "");
            renderString(response, retApp);
        }
        if (mobile == null || "".equals(mobile)) {
            RetApp retApp = new RetApp(FAIL, "手机号不能为空", "");
            renderString(response, retApp);
        }
        if (verifyCode == null || "".equals(verifyCode)) {
            RetApp retApp = new RetApp(FAIL, "验证码不能为空", "");
            renderString(response, retApp);
        }
        String telnoKey = SmsUtil.SMS_VALIDATE_CODE + SmsUtil.SMS_CODE_MGT_OWNER_AUTH+ "." + mobile;
        // 校验短信验证码
        SmsUtil.checkSmsCode(mobile, telnoKey, verifyCode);
        
        Map<String, String> identify = bpHouseMapService.identifyHouse(houseMap);
        String status = identify.get("status");
        String msg = identify.get("msg");
        if ("S".equals(status)) {
            result.setStatus(OK);
            result.setMessage("认证成功!");
            return result;
        } else {
            result.setStatus(FAIL);
            result.setMessage("认证失败!");
            result.setData(msg);
            return result;
        }
        
        
        
    }


    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/st/csp/bp/owner/isOwner")
    @ResponseBody
    public RetApp isOwner(@RequestParam(required=true) String token, HttpServletRequest request) {
        RetApp result = new RetApp();
        String userId = UserTokenUtils.getUserIdByToken(token);
        Boolean owner = bpHouseMapService.isOwner(userId);
        if(owner.equals(Boolean.TRUE)){
            result.setStatus(OK);
            result.setMessage("业主!");
            return result;
        }else{
            result.setStatus(FAIL);
            result.setMessage("非业主!");
            return result;
        }
    }
}
