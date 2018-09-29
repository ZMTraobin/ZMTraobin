/**
 * .
 */
package com.cmig.future.csportal.api.common.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.pay.order.service.IOrderFormService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试Controller
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "common/test/test/")
public class AppTestController extends BaseExtendController{

    private Logger logger= LoggerFactory.getLogger(AppTestController.class);

    private String appid="146c74602a3c422b8e5aaf41d5eb73db";
    private String secret="f0ff6faaa34a4893b583b0568f61ca35";

    @Autowired
    private IOrderFormService orderFormService;

    /**
     * oauth2.0 demo
     * @param request
     * @return
     */
    @RequestMapping(value = "access_token_owner")
    public ModelAndView access_token_owner(HttpServletRequest request) {
        String flag=getParam(request,"flag","");
        String code=getParam(request,"code","");
        String serverUrl = Global.getProjectPath(request);

        String url=serverUrl+"/common/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
        String result= HttpUtil.post(url);
        JSONObject jsonObject=JSONObject.fromObject(result);
        String data=jsonObject.get("data").toString();
        JSONObject dataJsonObject=JSONObject.fromObject(data);
        String access_token=dataJsonObject.get("access_token").toString();

        url=serverUrl+"/common/oauth2/userinfo?access_token="+access_token;
        result= HttpUtil.post(url);
        jsonObject=JSONObject.fromObject(result);
        data=jsonObject.get("data").toString();
        dataJsonObject=JSONObject.fromObject(data);

        ModelAndView model=new ModelAndView("/common/oauth2/demo/access_token_owner_demo");
        model.addObject("flag", flag);
        model.addObject("openid", dataJsonObject.get("openid"));
        model.addObject("mobile", dataJsonObject.get("mobile"));
        model.addObject("nickName", dataJsonObject.get("nickName"));
        model.addObject("retTime", dataJsonObject.get("retTime"));
        model.addObject("isOwnerFlag", dataJsonObject.get("isOwnerFlag"));
        model.addObject("assetsName", dataJsonObject.get("assetsName"));
        model.addObject("sourceSystemHouseIds", dataJsonObject.get("sourceSystemHouseIds"));

        return model;
    }

}
