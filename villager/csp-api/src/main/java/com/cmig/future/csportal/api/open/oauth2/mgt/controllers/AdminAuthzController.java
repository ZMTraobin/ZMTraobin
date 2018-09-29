package com.cmig.future.csportal.api.open.oauth2.mgt.controllers;

import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.MD5;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.sys.openinfo.service.IOpenAppInfoService;
import com.cmig.future.csportal.module.sys.utils.AdminTokenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping(value = "${commonPath}/${managementPath}/oauth2")
public class AdminAuthzController extends BaseExtendController {
	
	@Autowired
	private IOpenAppInfoService openAppInfoService;
	
	/**
	 * 构建OAuth2授权请求 
	 * @param request
	 * @param model
	 * http://localhost:8080/ljhui-api/common/mgt/oauth2/authorize?appid=10000000&scope=snsapi_userinfo&redirect_uri=http://ljlife.cm-dev.cn/ljlife
	 * @return
	 */
    @RequestMapping(value = "/authorize")
    public ModelAndView authorize(HttpServletRequest request,Model model){
    	
        final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID,"");
        logger.debug("{} : {}", OAuthConstants.OAUTH_APP_ID, appId);

        final String redirectUri = getParam(request,OAuthConstants.OAUTH_REDIRECT_URI,"");
        logger.debug("{} : {}", OAuthConstants.OAUTH_REDIRECT_URI, redirectUri);

        final String scope = getParam(request,OAuthConstants.OAUTH_SCOPE,"");
        logger.debug("{} : {}", OAuthConstants.OAUTH_SCOPE, scope);
        
        // app id不能为空
        if (StringUtils.isBlank(appId)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_APP_ID);
            model.addAttribute("errorMsg", "appId不能为空！");
            return new ModelAndView("common/oauth2/error");
        }
        
        //验证appId是否正确
        if(!OAuthUtils.validateOAuth2AppID(appId)){
        	logger.error("clientId is error {}", OAuthConstants.OAUTH_APP_ID);
            model.addAttribute("errorMsg", "appId不存在或错误！");
            return new ModelAndView("common/oauth2/error");
        }
        
        //验证回调地址
        if (StringUtils.isBlank(redirectUri)) {
            logger.error("Missing {}", OAuthConstants.OAUTH_REDIRECT_URI);
            model.addAttribute("errorMsg", "回调地址不能为空");
            return new ModelAndView("common/oauth2/error");
        }

        //获取用户token
        String userToken = getParam(request,"token","");
        if(StringUtils.isBlank(userToken)){
        	logger.error("用户token不能为空");
            model.addAttribute("errorMsg", "用户token不能为空");
            return new ModelAndView("common/oauth2/error");
        }

        //用户token是否有效
        String uid = AdminTokenUtils.getUserIdByToken(userToken);
        if(StringUtils.isBlank(uid)){
        	logger.error("用户未在客户端登录");
            model.addAttribute("errorMsg", "用户未登录，请在中民服务客户端登录");
            return new ModelAndView("common/oauth2/error");
        }
      
        String authorizationCode = MD5.MD5Encode(appId
        		+UUID.randomUUID().toString()+System.currentTimeMillis());
        //授权码缓存5分钟
        JedisUtils.set(OAuthConstants.CODE+authorizationCode, userToken, 300);
        String callbackUrl ="";
        if(redirectUri.indexOf("?")<0){
            callbackUrl = redirectUri+"?code="+authorizationCode;
        }else{
            callbackUrl = redirectUri+"&code="+authorizationCode;
        }
        return new ModelAndView("redirect:"+callbackUrl);
    }

    public static void main(String[] args) {
    	System.out.println(MD5.MD5Encode("10000000"+UUID.randomUUID().toString()
    			+System.currentTimeMillis()));
	}
	
}
