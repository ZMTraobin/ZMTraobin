package com.cmig.future.csportal.api.common.controllers;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.wx.config.WxConfig;
import com.cmig.future.csportal.common.wx.help.WxApiHelper;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}")
public class OpenWxOAuthController extends BaseExtendController {

	@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/open/wx/getAuthorizeUrl", produces = { "application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
	public RetApp getAuthorizeUrl(HttpServletRequest request)throws Exception  {
		JSONObject jsonObject=new JSONObject();
	    String redirectUri=getParam(request,"redirectUri","");
		String wxAppId=getParam(request,"wxAppId","");
	    checkNull(redirectUri);
		checkNull(wxAppId);
	    StringBuffer authorizerUrl=new StringBuffer();
	    authorizerUrl.append(WxConfig.AUTHORIZE_API+"?");
	    authorizerUrl.append("appid="+wxAppId);
	    authorizerUrl.append("&redirect_uri="+redirectUri);
	    authorizerUrl.append("&response_type=code");
	    authorizerUrl.append("&scope=snsapi_userinfo");
	    authorizerUrl.append("&state=STATE");
	    authorizerUrl.append("#wechat_redirect");
	    jsonObject.put("authorizerUrl",authorizerUrl.toString());
	    return RetAppUtil.success(jsonObject,"获取成功");
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/open/wx/getUserInfo", produces = { "application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
	public RetApp getUserInfo(HttpServletRequest request)throws Exception {
		String code=getParam(request,"code","");
		String wxAppId=getParam(request,"wxAppId","");
		checkNull(code);
		checkNull(wxAppId);
		String wxAppSecret= CodeUtil.getDictLabel(wxAppId,"CSP.WX.APPID.CONFIG",null,null);
		if(StringUtils.isEmpty(wxAppSecret)) {
			throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"wxAppId或者未配置私钥");
		}
		JSONObject resultObj = WxApiHelper.getH5AccessTokenByCode(code,wxAppId,wxAppSecret);
		String openid=resultObj.get("openid").toString();
		String accessToken= WxApiHelper.getAccessToken(wxAppId,wxAppSecret);
		JSONObject userInfo = WxApiHelper.getUserInfo(openid, accessToken);
		return RetAppUtil.success(userInfo,"获取成功");
	}




}
