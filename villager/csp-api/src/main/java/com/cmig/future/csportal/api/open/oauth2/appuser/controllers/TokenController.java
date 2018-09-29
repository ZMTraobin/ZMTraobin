package com.cmig.future.csportal.api.open.oauth2.appuser.controllers;

import com.cmig.future.csportal.common.oauth2.OAuthConstants;
import com.cmig.future.csportal.common.oauth2.entity.OAuth2AccessToken;
import com.cmig.future.csportal.common.oauth2.entity.ResponseEntity;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.oauth2.service.TokenService;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.sys.openinfo.service.IOpenAppInfoService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "${commonPath}/oauth2")
public class TokenController extends BaseExtendController{

	@Autowired
	private IOpenAppInfoService openAppInfoService;

	@Autowired
	private TokenService tokenService;

	/**
	 * 获取授权令牌token
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/access_token", method = RequestMethod.POST)
	public String access_token(HttpServletRequest request, HttpServletResponse response) {

		ResponseEntity result = new ResponseEntity();

		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		logger.debug("{} : {}", OAuthConstants.OAUTH_APP_ID, appId);

		final String grantType = getParam(request, OAuthConstants.OAUTH_GRANT_TYPE, "");
		logger.debug("{} : {}", OAuthConstants.OAUTH_GRANT_TYPE, grantType);

		final String appSecret = getParam(request, OAuthConstants.OAUTH_APP_SECRET, "");

		final String code = getParam(request, OAuthConstants.OAUTH_CODE, "");
		logger.debug("{} : {}", OAuthConstants.OAUTH_CODE, code);

		// 校验请求参数
		try {
			verifyAccessTokenRequest(response, grantType, appId, appSecret, code);
		} catch (OAuth2Exception e) {
			result.setStatus(FAIL);
			result.setMessage(e.getMessage());
			return renderString(response, result);
		}
		//获取用户id
		String uid = UserTokenUtils.getUserIdByToken(JedisUtils.get(OAuthConstants.CODE+code));
		//创建token
		OAuth2AccessToken token = tokenService.createAccessToken(uid,appId);
		logger.debug("oauth2_access_token {} ", token.toString());
		Map<String, Object> resMap = buildJSONMessage(token);
		result.setData(resMap);
		result.setStatus(OK);
		return renderString(response, result);
	}

	/**
	 * 获取授权令牌token
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public String refresh_token(HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity result = new ResponseEntity();
		final String appId = getParam(request, OAuthConstants.OAUTH_APP_ID, "");
		logger.debug("{} : {}", OAuthConstants.OAUTH_APP_ID, appId);
		final String refreshToken = getParam(request, OAuthConstants.OAUTH_REFRESH_TOKEN, "");
		logger.debug("{} : {}", OAuthConstants.OAUTH_REFRESH_TOKEN, refreshToken);
		// 校验请求参数
		try {
			verifyRefreshTokenRequest(appId, refreshToken);
		} catch (OAuth2Exception e) {
			result.setStatus(FAIL);
			result.setMessage(e.getMessage());
			return renderString(response, result);
		}
		// 刷新token
		OAuth2AccessToken token = tokenService.refreshAccessToken(refreshToken);
		if(token==null){
			result.setStatus(FAIL);
			result.setMessage("不合法的refresh_token");
			return renderString(response, result);
		}
		logger.debug("oauth2_refresh_token {} ", token.toString());
		Map<String, Object> resMap = buildJSONMessage(token);
		result.setData(resMap);
		result.setStatus(OK);
		return renderString(response, result);
	}

	/**
	 * 获取token时，相关参数数据校验
	 * 
	 * @param response
	 * @param appId
	 * @param appSecret
	 * @param code
	 * @return
	 */
	private void verifyAccessTokenRequest(final HttpServletResponse response, final String grantType,
			final String appId, final String appSecret, final String code) {

		// grantType is required
		if (StringUtils.isBlank(grantType)) {
			logger.error("Missing {}", OAuthConstants.OAUTH_GRANT_TYPE);
			throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"grant_type");
		}
		if (!OAuthConstants.AUTHORIZATION_CODE.equals(grantType)) {
			logger.error("not supported {}", grantType);
			throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"只支持授权码模式");
		}

        // 校验appid、appSecret
        OAuthUtils.checkAppOauth(appId, appSecret);

		// code is required
		if (StringUtils.isBlank(code)) {
			logger.error("Missing {}", OAuthConstants.OAUTH_CODE);
			throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"oauth code");
		}

		// code 是否过期
		String userToken = JedisUtils.get(OAuthConstants.CODE+code);
		if (StringUtils.isBlank(userToken)) {
			logger.error("Missing {}", OAuthConstants.OAUTH_CODE);
			throw new OAuth2Exception(RestApiExceptionEnums.ARGS_ERROR,"oauth code超时");
		}
		
		//获取用户id
		String uid = UserTokenUtils.getUserIdByToken(userToken);
		if (StringUtils.isBlank(uid)) {
			logger.error("系统繁忙，用户未获取");
			throw new OAuth2Exception(RestApiExceptionEnums.GET_USER_BY_TOKEN_ERROR);
		}
	}

	/**
	 * 刷新token时，相关参数数据校验
	 * 
	 * @param appId
	 * @param refreshToken
	 */
	private void verifyRefreshTokenRequest(final String appId, final String refreshToken) {
		// 校验appid
        OAuthUtils.checkOAuth2AppID(appId);

		// refreshToken is required
		if (StringUtils.isBlank(refreshToken)) {
			logger.error("Missing {}", OAuthConstants.OAUTH_REFRESH_TOKEN);
			throw new OAuth2Exception(RestApiExceptionEnums.ARGS_NULL,"refresh_token");
		}
	}

	/**
	 * 转换成map输出到前端
	 * 
	 * @param token
	 * @return
	 */
	private Map<String, Object> buildJSONMessage(OAuth2AccessToken token) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(OAuthConstants.OAUTH_ACCESS_TOKEN, token.getAccessToken());
		parameters.put(OAuthConstants.OAUTH_REFRESH_TOKEN, token.getRefreshToken());
		parameters.put(OAuthConstants.OAUTH_EXPIRES_IN, token.getExpiresIn());
		return parameters;
	}

}
