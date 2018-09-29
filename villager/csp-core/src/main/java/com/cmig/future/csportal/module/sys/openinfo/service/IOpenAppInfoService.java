package com.cmig.future.csportal.module.sys.openinfo.service;

import java.util.List;

import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface IOpenAppInfoService extends IBaseService<OpenAppInfo>, ProxySelf<IOpenAppInfoService>{

	
	public List<OpenAppInfo> findList(OpenAppInfo openAppInfo);


	/**
	 * 分页查询
	 */

	public List<OpenAppInfo>selectOppInfo(IRequest requestContext, OpenAppInfo dto, int page, int pageSize);

	/**
	 *根据id查询对象
	 */

	public OpenAppInfo getOpenInfoById(OpenAppInfo dto);

	/**
	 * 添加商户接入
	 * @param dto
     */
	public void save(OpenAppInfo dto);

	
}
	

	
	
	
	
	
	
	
	
