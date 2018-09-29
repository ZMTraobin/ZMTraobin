package com.cmig.future.csportal.module.user.appuser.service.impl;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IThirdUuidLoginService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:31 2017/10/25.
 * @Modified by zhangtao on 18:31 2017/10/25.
 */
@Service("cspThirdUuidLoginService")
public class CspThirdUuidLoginServiceImpl implements IThirdUuidLoginService {

	@Override
	public boolean isBind(String thirdChannelCode, String appId, String code) {
		return false;
	}

	@Override
	public JSONObject login(String thirdChannelCode, String appId, String code) throws Exception {
		return null;
	}

	@Override
	public void bind(String thirdChannelCode, String appId, String code, String uid) throws Exception {

	}

	@Override
	public AppUser register(String thirdChannelCode, String appId, String code, String mobile, String password) throws Exception {
		return null;
	}

	@Override
	public void unbind(String uid, String thirdChannelCode, String appId, String code) throws Exception {

	}
}
