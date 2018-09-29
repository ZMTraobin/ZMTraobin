package com.cmig.future.csportal.module.sys.appconfig.service.impl;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppCommunityFunction;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigCommunity;
import com.cmig.future.csportal.module.sys.appconfig.mapper.AppConfigCommunityMapper;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppConfigCommunityService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppConfigCommunityServiceImpl extends BaseServiceImpl<AppConfigCommunity> implements IAppConfigCommunityService {

     @Autowired
     private AppConfigCommunityMapper appConfigCommunityMapper;
    /**
     * 分页查询业主端的数据
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<AppConfigCommunity> selectOwner(IRequest requestContext, AppConfigCommunity dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return appConfigCommunityMapper.selectOwner(dto);
    }

    /**
     * 分页查询物业端的数据
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<AppConfigCommunity> selectPropertyMC(IRequest requestContext, AppConfigCommunity dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return appConfigCommunityMapper.selectPropertyMC(dto);
    }

    /**
     * 根据主键查询业主端信息
     * @param dto
     * @return
     */
    @Override
    public AppConfigCommunity getAppConfigCommunityById(AppConfigCommunity dto) {
        return appConfigCommunityMapper.getAppConfigCommunityById(dto);
    }

    /**
     * 更具小区配置Id，查询小区配置的详细信息
     * @param dto
     * @return
     */
    @Override
    public AppConfigCommunity getAppConfigCommunityByIdPropertyMC(AppConfigCommunity dto) {
        return appConfigCommunityMapper.getAppConfigCommunityByIdPropertyMC(dto);
    }

    /**
     * 根据小区配置ID，更新小区配置关系的delete_flag
     * @param appConfigCommunityId
     */
    @Override
    public void updateAppCommunityFunction(String appConfigCommunityId) {
        appConfigCommunityMapper.updateAppCommunityFunction(appConfigCommunityId);
    }

    /**
     * 根据小区配置Id，对象中的CommunityId(小区ID)，查询该小区开通了哪些app功能
     * @param dto
     * @return
     */
    @Override
    public List<AppConfigCommunity> findAppCommunity(AppConfigCommunity dto) {
        return appConfigCommunityMapper.findAppCommunity(dto);
    }
}