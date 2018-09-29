package com.cmig.future.csportal.module.sys.appconfig.service;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IAppConfigFunctionService extends IBaseService<AppConfigFunction>, ProxySelf<IAppConfigFunctionService>{

    /**
     * 查询业主端的数据
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<AppConfigFunction> selectOwner(IRequest requestContext, AppConfigFunction dto, int page, int pageSize);
    /**
     * 查询物业端的数据
     * @return
     */
    List<AppConfigFunction> selectMC(IRequest requestContext, AppConfigFunction dto, int page, int pageSize);

    /**
     * 根据id查询业主端配置的app功能
     * @param id
     * @return
     */
    List<AppConfigFunction>selectAppConfigById(String id, int pageSize, int page);

    /**
     * 根据ID查询物业端app配置功能的详细信息
     * 也就是查看功能
     * @param id
     * @return
     */
    List<AppConfigFunction>selectAppConfigByIdPropertyMC(String id, int pageSize, int page);

    /**
     * 根据小区配置ID查询出小区配置了哪些功能
     * @param id
     * @return
     */
    List<AppConfigFunction>getCommunityConfig(String id);

    /**
     * 查询物业端小区开通了哪些app功能
     * @param id
     * @return
     */
    List<AppConfigFunction>getCommunityConfigMC(String id);

    /**
     * 查询小区开通功能之外的功能
     * @param id
     * @return
     */
    List<AppConfigFunction>findOtherCommuntiyConfig(String id);

    /**
     * 查询物业端小区开通了app之外的功能
     * @param id
     * @return
     */
    List<AppConfigFunction>findOtherCommuntiyConfigMC(String id);



    List<AppConfigFunction> findList(AppConfigFunction function);

    /**
     * 添加业主端应用
     * @param dto
     */
    public void save(AppConfigFunction dto);
}