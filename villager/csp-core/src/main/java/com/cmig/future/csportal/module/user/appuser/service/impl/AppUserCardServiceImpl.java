package com.cmig.future.csportal.module.user.appuser.service.impl;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.module.user.appuser.dto.AppUserCard;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserCardMapper;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserCardService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppUserCardServiceImpl extends BaseServiceImpl<AppUserCard> implements IAppUserCardService{

	@Autowired
	private AppUserCardMapper appUserCardMapper;

	@Override
	public List<AppUserCard> find(AppUserCard appUserCard) {
		return appUserCardMapper.find(appUserCard);
	}

	@Override
	public List<AppUserCard> findList(IRequest requestCtx, AppUserCard appUserCard, int page, int pagesize){
		PageHelper.startPage(page, pagesize);
		return appUserCardMapper.find(appUserCard);
	}

	@Override
	public AppUserCard findDefault(String appUserId) {
		List<AppUserCard> list=appUserCardMapper.findDefault(appUserId);
		if(null!=list&&list.size()>0){
			return list.get(0);
		}
		return null ;
	}

	@Override
	public void updateDefaultToN(String appUserId) {
		appUserCardMapper.updateDefaultToN(appUserId);
	}

	@Override
	public void updateDefaultToY(Long cardId) {
		appUserCardMapper.updateDefaultToY(cardId);
	}

	@Override
	public void setDefaultCard(String appUserId, String cardId)throws Exception {
		AppUserCard appUserCard= appUserCardMapper.selectByPrimaryKey(cardId);
		if(null==appUserCard){
			throw new DataWarnningException("cardId 参数错误");
		}else if(!appUserCard.getAppUserId().equals(appUserId)){
			throw new DataWarnningException("cardId、appUserId 参数错误");
		}
		appUserCardMapper.updateDefaultToN(appUserId);
		appUserCardMapper.updateDefaultToY(new Long(cardId));
	}

	@Override
	public AppUserCard selectByPrimaryKey(Long cardId) {
		return appUserCardMapper.selectByPrimaryKey(cardId);
	}

	/**
	 * 查询用户第一次绑卡的信息做为用户基本信息，不排除已解绑
	 * @param appUserId
	 * @return
	 */
	@Override
	public AppUserCard getUserIdInfo(String appUserId) {
		return appUserCardMapper.findFirstCard(appUserId);
	}
}