package com.cmig.future.csportal.module.user.appuser.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserAuth;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserAuthMapper;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserAuthService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppUserAuthServiceImpl extends BaseServiceImpl<AppUserAuth> implements IAppUserAuthService{

	@Autowired
	private AppUserAuthMapper appUserAuthMapper;

	@Override
	public AppUserAuth findByUUIDAndType(String uuid, String identityType) {
		return appUserAuthMapper.findByUUIDAndType(uuid,identityType);
	}

	@Override
	public void save(AppUserAuth appUserAuth) {
		if(!StringUtils.isEmpty(appUserAuth.getAuthId())){
			appUserAuthMapper.updateByPrimaryKeySelective(appUserAuth);
		}else{
			appUserAuth.setAuthId(IdGen.uuid());
			appUserAuthMapper.insertSelective(appUserAuth);
		}
	}

	@Override
	public void unbundled(String identityType, String uuid, String token) {
		String appUserId= UserTokenUtils.getUserIdByToken(token);
		appUserAuthMapper.unbundled(identityType,uuid,appUserId);
	}
}