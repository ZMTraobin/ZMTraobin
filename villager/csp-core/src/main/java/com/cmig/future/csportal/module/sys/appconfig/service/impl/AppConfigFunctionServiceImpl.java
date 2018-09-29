package com.cmig.future.csportal.module.sys.appconfig.service.impl;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.cmig.future.csportal.module.sys.appconfig.mapper.AppConfigFunctionMapper;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigFunctionService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppConfigFunctionServiceImpl extends BaseServiceImpl<AppConfigFunction> implements IAppConfigFunctionService{

	@Autowired
	private AppConfigFunctionMapper appConfigFunctionMapper;

	/**
	 * 查询业主端的数据
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
     * @return
     */
	@Override
	public List<AppConfigFunction> selectOwner(IRequest requestContext, AppConfigFunction dto, int page, int pageSize) {
		PageHelper.startPage(page,pageSize);
	    return appConfigFunctionMapper.selectOwner(dto);
	}

	/**
	 * 查询物业端的数据
	 * @param requestContext
	 * @param dto
	 * @param page
	 * @param pageSize
     * @return
     */
	@Override
	public List<AppConfigFunction> selectMC(IRequest requestContext, AppConfigFunction dto, int page, int pageSize) {
		PageHelper.startPage(page,pageSize);
		return appConfigFunctionMapper.selectMC(dto);
	}

	@Override
	public List<AppConfigFunction> selectAppConfigById(String id,int pageSize,int page) {
		PageHelper.startPage(page,pageSize);
		return appConfigFunctionMapper.selectAppConfigById(id);
	}

	/**
	 * 更具小区配置功能Id查询小区配置的详细信息
	 * 查看功能
	 * @param id
	 * @return
     */
	@Override
	public List<AppConfigFunction> selectAppConfigByIdPropertyMC(String id,int pageSize,int page) {

		return appConfigFunctionMapper.selectAppConfigByIdPropertyMC(id);
	}

	/**
	 * 根据小区配置ID查询出小区配置了哪些功能
	 * @param id
	 * @return
     */
	@Override
	public List<AppConfigFunction> getCommunityConfig(String id) {
		return appConfigFunctionMapper.getCommunityConfig(id);
	}

	/**
	 * 查询物业端小区开通了哪些app功能
	 * @param id
	 * @return
     */
	@Override
	public List<AppConfigFunction> getCommunityConfigMC(String id) {
		return appConfigFunctionMapper.getCommunityConfigMC(id);
	}

	/**
	 * 查询小区开通功能之外的功能
	 * @param id
	 * @return
     */
	@Override
	public List<AppConfigFunction> findOtherCommuntiyConfig(String id) {
		return appConfigFunctionMapper.findOtherCommuntiyConfig(id);
	}

	/**
	 * 查询物业端小区开通了app功能之外的功能
	 * @param id
	 * @return
     */
	@Override
	public List<AppConfigFunction> findOtherCommuntiyConfigMC(String id) {
		return appConfigFunctionMapper.findOtherCommuntiyConfigMC(id);
	}

	@Override
    public List<AppConfigFunction> findList(AppConfigFunction function) {
        return appConfigFunctionMapper.findList(function);
    }

	/**
	 * 添加业主端应用
	 * @param dto
     */
	@Override
	public void save(AppConfigFunction dto) {
		super.mapper.insertSelective(dto);
	}
}