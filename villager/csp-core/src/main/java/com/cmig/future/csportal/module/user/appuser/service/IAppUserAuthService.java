package com.cmig.future.csportal.module.user.appuser.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserAuth;

public interface IAppUserAuthService extends IBaseService<AppUserAuth>, ProxySelf<IAppUserAuthService>{

	AppUserAuth findByUUIDAndType(String uuid,String identityType);

	void save(AppUserAuth appUserAuth);

	/**
	 * 解绑
	 * @param identityType
	 * @param uuid
	 * @param token
	 */
	void unbundled(String identityType, String uuid, String token);
}