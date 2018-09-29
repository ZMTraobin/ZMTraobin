package com.cmig.future.csportal.module.sys.appconfig.service;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppCommunityFunction;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigCommunity;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IAppConfigCommunityService extends IBaseService<AppConfigCommunity>, ProxySelf<IAppConfigCommunityService>{
    /**
     * 分页查询业主端的数据
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<AppConfigCommunity> selectOwner(IRequest requestContext, AppConfigCommunity dto, int page, int pageSize);

    /**
     * 分页查询物业端的数据
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSiz
     * @return
     */
    List<AppConfigCommunity>selectPropertyMC(IRequest requestContext, AppConfigCommunity dto, int page, int pageSiz);
    /**
     * 根据主键查询业主端信息
     * @param dto
     * @return
     */
    AppConfigCommunity getAppConfigCommunityById(AppConfigCommunity dto);

    /**
     * 根据小区配置ID查询小区详细信息
     * @return
     */
    AppConfigCommunity getAppConfigCommunityByIdPropertyMC(AppConfigCommunity dto);


    /**
     * 根据小区配置Id，更新小区配置的删除标识
     */
    void updateAppCommunityFunction(String appConfigCommunityId);

    /**
     * 根据小区配置ID查询该小区配置了哪些app功能
     * @param dto
     * @return
     */
    List<AppConfigCommunity> findAppCommunity(AppConfigCommunity dto);

}