package com.cmig.future.csportal.module.sys.appconfig.service;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppCommunityFunction;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface IAppCommunityFunctionService extends IBaseService<AppCommunityFunction>, ProxySelf<IAppCommunityFunctionService>{

	/**
	 * 批量配置快捷服务
	 * @param functionIds
	 * @param appConfigCommunityIds
	 * @param orderNums
	 */
	void batchAdd(String[] functionIds, String[] appConfigCommunityIds,String[] orderNums);

	/**
	 * 批量删除快捷服务
	 * @param appConfigCommunityIds
	 */
	void batchDeleteByCid(String[] appConfigCommunityIds);

	/**
	 * 单条配置快捷服务
	 * @param functionIds
	 * @param appConfigCommunityId
	 * @param orderNums
	 */
	void updateCommuntiyConfig(String[] functionIds, String appConfigCommunityId, String[] orderNums);
}