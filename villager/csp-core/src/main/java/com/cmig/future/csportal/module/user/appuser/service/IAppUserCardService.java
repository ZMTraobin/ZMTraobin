package com.cmig.future.csportal.module.user.appuser.service;

import com.cmig.future.csportal.module.user.appuser.dto.AppUserCard;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IAppUserCardService extends IBaseService<AppUserCard>, ProxySelf<IAppUserCardService>{

	List<AppUserCard> find(AppUserCard appUserCard);

	/**
	 * 后端分页
	 * @param appUserCard
	 * @return
     */
	List<AppUserCard> findList(IRequest requestCtx, AppUserCard appUserCard, int page, int pagesize);

	/**
	 * 查询默认的银行卡信息
	 * @param appUserId
	 * @return
	 */
	AppUserCard findDefault(String appUserId);

	/**
	 * 更新用户的卡是否默认值为否
	 * @param appUserId
	 */
	void  updateDefaultToN(String appUserId);

	/**
	 * 更新该卡是否默认值为是
	 * @param cardId
	 */
	void  updateDefaultToY(Long cardId);


	/**
	 * 设置用户默认卡信息
	 * @param appUserId
	 * @param cardId
	 */
	void setDefaultCard(String appUserId, String cardId)throws Exception ;

	AppUserCard selectByPrimaryKey(Long cardId);

	/**
	 * 查询用户第一次绑卡的信息做为用户基本信息，不排除已解绑
	 * @param appUserId
	 * @return
	 */
	AppUserCard getUserIdInfo(String appUserId);
}