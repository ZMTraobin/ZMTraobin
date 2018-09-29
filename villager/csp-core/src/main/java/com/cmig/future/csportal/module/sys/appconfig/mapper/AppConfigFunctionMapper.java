package com.cmig.future.csportal.module.sys.appconfig.mapper;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppConfigFunction;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppConfigFunctionMapper extends Mapper<AppConfigFunction>{
	/**
	 * 查询业务端的数据
	 * @param dto
	 * @return
     */
	List<AppConfigFunction> selectOwner(AppConfigFunction dto);
	/**
	 * 查询物业的数据
	 * @param dto
	 * @return
	 */
	List<AppConfigFunction>selectMC(AppConfigFunction dto);

	/**
	 * 根据id查询业主端配置的app功能
	 * @param id
	 * @return
     */
	List<AppConfigFunction>selectAppConfigById(@Param(value = "id") String id);
	/**
	 * 根据小区配置ID查询出小区配置了哪些功能
	 * @param id
	 * @return
	 */
	List<AppConfigFunction>getCommunityConfig(@Param(value = "id") String id);

	/**
	 * 查询业主端小区开通功能的之外的功能
	 * @param id
	 * @return
     */
	List<AppConfigFunction>findOtherCommuntiyConfig(@Param(value = "id") String id);

	/**
	 * 查询物业端小区开通app功能之外的功能
	 * @param id
	 * @return
     */
	List<AppConfigFunction>findOtherCommuntiyConfigMC(@Param(value = "id") String id);

	/**
	 * 查询物业端小区开通了哪些功能
	 * @param id
	 * @return
     */
	List<AppConfigFunction>getCommunityConfigMC(@Param(value = "id") String id);


	/**
	 * 根据ID查询物业端app配置功能的详细信息
	 * 也就是查看功能
	 * @param id
	 * @return
	 */
	List<AppConfigFunction>selectAppConfigByIdPropertyMC(String id);
    /**
     * app接口，查询对客端配置功能列表
     * @param function
     * @return
     */
    List<AppConfigFunction> findList(AppConfigFunction function);
}