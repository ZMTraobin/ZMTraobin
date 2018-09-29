package com.cmig.future.csportal.module.user.appuser.service;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import net.sf.json.JSONObject;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:17 2017/10/25.
 * @Modified by zhangtao on 18:17 2017/10/25.
 */
public interface IThirdUuidLoginService {

	/**
	 * 查询第三方账户是否已绑定
	 * @param thirdChannelCode
	 * @param appId
	 * @param code
	 * @return
	 */
	boolean isBind(String thirdChannelCode,String appId,String code);

	/**
	 * 第三方帐号登录
	 * @param thirdChannelCode
	 * @param appId
	 * @param code
	 * @return
	 */
	JSONObject login(String thirdChannelCode,String appId, String code) throws Exception;

	/**
	 * 第三方帐号绑定已注册用户
	 * @param thirdChannelCode
	 * @param appId
	 * @param code
	 * @param uid
	 * @return
	 */
	void bind(String thirdChannelCode, String appId, String code, String uid) throws Exception;

	/**
	 * 第三方帐号注册
	 * @param thirdChannelCode
	 * @param appId
	 * @param code
	 * @param mobile
	 * @param password
	 * @return
	 */
	AppUser register(String thirdChannelCode,String appId, String code, String mobile,String password) throws Exception;

	/**
	 * 第三方帐号解绑
	 * @param uid
	 * @param thirdChannelCode
	 * @param appId
	 * @param code
	 */
	void unbind(String uid,String thirdChannelCode,String appId,String code) throws Exception;

}
