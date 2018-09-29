package com.cmig.future.csportal.module.user.appuser.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserAuth;
import org.apache.ibatis.annotations.Param;

public interface AppUserAuthMapper extends Mapper<AppUserAuth>{


	/**
	 * 通过UUID查询本地帐号绑定信息
	 */
	AppUserAuth findByUUIDAndType(@Param(value = "uuid") String uuid,@Param(value = "identityType") String identityType);


	/**
	 * 解绑
	 * @param identityType
	 * @param uuid
	 * @param appUserId
	 */
	void unbundled(@Param(value = "identityType") String identityType,  @Param(value = "uuid") String uuid, @Param(value = "appUserId") String appUserId);
}