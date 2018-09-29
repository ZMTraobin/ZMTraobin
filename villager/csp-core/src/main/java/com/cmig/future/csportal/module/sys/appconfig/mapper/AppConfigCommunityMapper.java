package com.cmig.future.csportal.module.sys.appconfig.mapper;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppCommunityFunction;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigCommunity;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface AppConfigCommunityMapper extends Mapper<AppConfigCommunity>{
    /**
     * 分页查询业主端的数据
     * @param dto
     * @return
     */
    List<AppConfigCommunity> selectOwner(AppConfigCommunity dto);

    /**
     * 分页查询物业端的数据
     * @param dto
     * @return
     */
    List<AppConfigCommunity>selectPropertyMC(AppConfigCommunity dto);

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
     * 根据小区配置关系表，更新小区配置表中的删除字段，设置为1
     * @param AppConfigCommuntiyId
     */
    void updateAppCommunityFunction(String AppConfigCommuntiyId);


    /**
     * 根据小区配置ID查询该小区配置了哪些app功能
     * @param dto
     * @return
     */
    List<AppConfigCommunity> findAppCommunity(AppConfigCommunity dto);





}