package com.cmig.future.csportal.common.wx.help;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.wx.config.WxConfig;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 9:42 2017/8/24.
 * @Modified by zhangtao on 9:42 2017/8/24.
 */
public class WxApiHelper {

	private static final String WX_API_ACCESS_TOKEN_KEY_PREFIX="csp.wx.api.accessToken_";

	/**
	 *
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getH5AccessTokenByCode(String code,String appid,String secret) throws Exception {
		StringBuffer openidUrl=new StringBuffer();
		openidUrl.append(WxConfig.OPEN_ID_API+"?");
		openidUrl.append("appid="+appid);
		openidUrl.append("&secret="+secret);
		openidUrl.append("&code="+code);
		openidUrl.append("&grant_type=authorization_code");
		String result= HttpUtil.get(openidUrl.toString());
		JSONObject resultObj= JSONObject.parseObject(result);
		if(null!=resultObj.get("errcode")){
			throw new ServiceException(RestApiExceptionEnums.GET_WX_CODE_TO_ACCESS_TOKEN_ERROR,resultObj.get("errmsg").toString());
		}
		return resultObj;
	}


	/**
	 * 获取access_token
	 * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。
	 * access_token的存储至少要保留512个字符空间。
	 * access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。
	 * @param appid
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken(String appid,String secret) throws Exception {
		String key=WX_API_ACCESS_TOKEN_KEY_PREFIX+appid;
		String accessToken=JedisUtils.get(key);
		if(!StringUtils.isEmpty(accessToken)){
			return accessToken;
		}
		StringBuffer accessTokenUrl=new StringBuffer();
		accessTokenUrl.append(WxConfig.ACCESS_TOKEN_API+"?");
		accessTokenUrl.append("grant_type=client_credential");
		accessTokenUrl.append("&appid="+appid);
		accessTokenUrl.append("&secret="+secret);
		String result= HttpUtil.get(accessTokenUrl.toString());
		JSONObject resultObj= JSONObject.parseObject(result);
		if(null!=resultObj.get("errcode")){
			throw new ServiceException(RestApiExceptionEnums.GET_WX_API_ACCESS_TOKEN_ERROR,resultObj.get("errmsg").toString());
		}
		accessToken=resultObj.get("access_token")==null?"":resultObj.get("access_token").toString();
		String expiresIn=resultObj.get("expires_in")==null?"":resultObj.get("expires_in").toString();

		//缓存accessToken，并提前2分钟刷新token
		if(!StringUtils.isEmpty(accessToken)&&!StringUtils.isEmpty(expiresIn)){
			JedisUtils.set(key,accessToken,new Integer(expiresIn)-120);
		}

		return accessToken;
	}

	/**
	 * 获取用户基本信息(UnionID机制)
	 * UnionID机制说明：
	 * 开发者可通过OpenID来获取用户基本信息。特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，
	 * 可通过获取用户基本信息中的unionid来区分用户的唯一性，因为只要是同一个微信开放平台帐号下的移动应用、
	 * 网站应用和公众帐号，用户的unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。
	 * @param openid
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getUserInfo(String openid, String accessToken) throws Exception {
		StringBuffer userInfoUrl=new StringBuffer();
		userInfoUrl.append(WxConfig.USER_INFO_API+"?");
		userInfoUrl.append("access_token="+accessToken);
		userInfoUrl.append("&openid="+openid);
		userInfoUrl.append("&lang=zh_CN");
		String userInfoStr= HttpUtil.get(userInfoUrl.toString());
		JSONObject userInfo=JSONObject.parseObject(userInfoStr);

		if(null!=userInfo.get("errcode")){
			throw new ServiceException(RestApiExceptionEnums.GET_WX_USER_INFO_ERROR,userInfo.get("errmsg").toString());
		}
		return userInfo;
	}
}
