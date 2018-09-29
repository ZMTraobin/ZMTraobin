package com.cmig.future.csportal.api.app.user.service;

import com.cmig.future.csportal.api.app.user.dto.UserInfoReq;
import com.hand.hap.core.ProxySelf;

import net.sf.json.JSONObject;

/**
 * class name: IAppUserInfoService 
 * author: fri
 * date: 2017年4月6日
 * function: 设置接口
 */
public interface IAppUserInfoService extends ProxySelf<IAppUserInfoService>{
	
	/**
	 * class name: queryUserMyWealth
	 * author: fri
	 * date: 2017年4月7日
	 * function: 查询我的财富总接口
	 * in:
	 *		parameter1: retype 该访问请求类型判别 1财富总览 2理财产品明细
	 *		parameter2: userInfoReq 用户参数请求
	 *		parameter3: result 绑定参数
	 *		parameter4: request 请求
	 *		parameter5: response 响应
	 * out:
	 *		parameter: JSONObject 返回数据
	 */
	JSONObject queryUserMyWealth(Integer retype,UserInfoReq userInfoReq);
}
