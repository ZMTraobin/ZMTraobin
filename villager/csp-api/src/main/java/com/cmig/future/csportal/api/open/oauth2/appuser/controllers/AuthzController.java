package com.cmig.future.csportal.api.open.oauth2.appuser.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.MD5;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.openinfo.service.IOpenAppInfoService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping(value = "${commonPath}/oauth2")
public class AuthzController extends BaseExtendController{
	
	@Autowired
	private IOpenAppInfoService openAppInfoService;
	
	/**
	 * 构建OAuth2授权请求 
	 * @param request
	 * @param model
	 * http://localhost:8080/ljhui-api/common/oauth2/authorize?appid=10000000&scope=snsapi_userinfo&redirect_uri=http://ljlife.cm-dev.cn/ljlife
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
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
        String userToken = request.getHeader("token");
        
        String appName= Global.getProductName();
        model.addAttribute("app_name",appName);
        model.addAttribute("app_id",appId);
        model.addAttribute("redirect_uri",redirectUri);
        model.addAttribute("scope",scope);
        
        /**判断此次请求是否是用户授权
         * scope应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
         * snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地
         */
        if(scope!=null&&scope.equalsIgnoreCase("snsapi_userinfo")){
        	if(request.getParameter("action")==null||!request.getParameter("action").equalsIgnoreCase("authorize")){
        		//到申请用户同意授权页
                return new ModelAndView("common/oauth2/authorize");
        	}
        	userToken = request.getParameter("token");
            if(StringUtils.isEmpty(userToken)){
            	logger.error("userToken is null");
                model.addAttribute("errorMsg", "请用"+Global.getProductName()+"客户端访问");
                return new ModelAndView("common/oauth2/error");
            }
            
            try {
                JSONObject userInfo = JSONObject.fromObject(userToken);
                userToken = userInfo.getString("user_token");
    		} catch (Exception e) {
    			logger.error("解析用户数据错误");
    		}
        }else{
        	userToken = request.getHeader("token");
        }

        //用户token是否有效
        String uid = UserTokenUtils.getUserIdByToken(userToken);
        if(StringUtils.isEmpty(uid)){
        	logger.error("用户未在客户端登录");
            model.addAttribute("errorMsg", "用户未登录，请在"+Global.getProductName()+"客户端登录");
            return new ModelAndView("common/oauth2/error");
        }
      
        String authorizationCode = MD5.MD5Encode(appId
        		+UUID.randomUUID().toString()+System.currentTimeMillis());
        //授权码缓存5分钟
        JedisUtils.set(OAuthConstants.CODE+authorizationCode, userToken, 300);
        String callbackUrl ="";
        if(redirectUri.indexOf("?")<0){
            callbackUrl = StringEscapeUtils.unescapeHtml(redirectUri)+"?code="+authorizationCode;
        }else{
            callbackUrl = StringEscapeUtils.unescapeHtml(redirectUri)+"&code="+authorizationCode;
        }

        return new ModelAndView("redirect:"+callbackUrl);
    }


	/**
	 * 构建OAuth2授权请求
	 * @param request
	 * @param model
	 * http://localhost:8080/ljhui-api/common/oauth2/authorize?appid=10000000&scope=snsapi_userinfo&redirect_uri=http://ljlife.cm-dev.cn/ljlife
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/wx/authorize" ,method = {RequestMethod.GET,RequestMethod.POST})
	public RetApp wxAuthorize(HttpServletRequest request, Model model) throws Exception {

		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID,"");
		logger.debug("{} : {}", OAuthConstants.OAUTH_APP_ID, appId);

		final String redirectUri = getParam(request,OAuthConstants.OAUTH_REDIRECT_URI,"");
		logger.debug("{} : {}", OAuthConstants.OAUTH_REDIRECT_URI, redirectUri);

		final String scope = getParam(request,OAuthConstants.OAUTH_SCOPE,"");
		logger.debug("{} : {}", OAuthConstants.OAUTH_SCOPE, scope);

		//验证appId是否正确
		OAuthUtils.checkOAuth2AppID(appId);

		//验证回调地址
		if (StringUtils.isBlank(redirectUri)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,OAuthConstants.OAUTH_REDIRECT_URI);
		}

		//获取用户token
		String userToken = request.getHeader("token");

		String appName= Global.getProductName();
		model.addAttribute("app_name",appName);
		model.addAttribute("app_id",appId);
		model.addAttribute("redirect_uri",redirectUri);
		model.addAttribute("scope",scope);


		//用户token是否有效
		String uid = UserTokenUtils.getUserIdByToken(userToken);
		if(StringUtils.isEmpty(uid)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"用户未在客户端登录");
		}

		String authorizationCode = MD5.MD5Encode(appId+UUID.randomUUID().toString()+System.currentTimeMillis());
		//授权码缓存5分钟
		JedisUtils.set(OAuthConstants.CODE+authorizationCode, userToken, 300);
		String callbackUrl ="";
		if(redirectUri.indexOf("?")<0){
			callbackUrl = StringEscapeUtils.unescapeHtml(redirectUri)+"?code="+authorizationCode;
		}else{
			callbackUrl = StringEscapeUtils.unescapeHtml(redirectUri)+"&code="+authorizationCode;
		}

		String result=HttpUtil.get(callbackUrl);
		JSONObject jsonObject=JSONObject.fromObject(result);
		return RetAppUtil.success(jsonObject,"获取物管用户信息成功");

	}

    public static void main(String[] args) {
    	System.out.println(MD5.MD5Encode("10000000"+UUID.randomUUID().toString()
    			+System.currentTimeMillis()));
	}
	
}
