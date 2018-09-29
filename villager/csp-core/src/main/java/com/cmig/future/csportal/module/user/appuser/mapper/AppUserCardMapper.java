package com.cmig.future.csportal.module.user.appuser.mapper;

import com.cmig.future.csportal.module.user.appuser.dto.AppUserCard;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppUserCardMapper extends Mapper<AppUserCard>{

	List<AppUserCard> find(AppUserCard appUserCard);


	List<AppUserCard> findDefault(@Param(value = "appUserId") String appUserId);

	void updateDefaultToN(@Param(value = "appUserId") String appUserId);

	void updateDefaultToY(@Param(value = "cardId") Long cardId);

	/**
	 * 查询用户第一次绑卡的信息做为用户基本信息，不排除已解绑
	 * @param appUserId
	 * @return
	 */
	AppUserCard findFirstCard(@Param(value = "appUserId") String appUserId);
}