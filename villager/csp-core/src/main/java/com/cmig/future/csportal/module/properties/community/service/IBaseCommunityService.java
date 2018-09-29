package com.cmig.future.csportal.module.properties.community.service;

import com.cmig.future.csportal.module.properties.base.customer.CustomerInfoException;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;


/**
 * IBaseCommunityService
 *
 * @author bubu
 *
 * 2017-3-21
 */
public interface IBaseCommunityService extends IBaseService<BaseCommunity>, ProxySelf<IBaseCommunityService>{

    /**
     * 項目信息保存
     * @param community
     */
    void save(BaseCommunity community);


    /**
     * 根据源系統ID和源系統代碼查詢項目
     * @param community
     * @return
     */
    BaseCommunity getBySourceSystemId(BaseCommunity community);

    /**
     * 根据物业公司和小区名称查询小区信息
     * @param community
     * @return
     */
    BaseCommunity findByCommunityName(BaseCommunity community);

    /**
     * 根据物业公司名稱查询小区信息
     * @param community
     * @return
     */
    List<BaseCommunity> findCommunityListByName(BaseCommunity community);

    /**
     * 項目信息刪除
     * @param community
     * @return
     */
    int delete(BaseCommunity community);

    /**
     * 查询员工关联的项目信息
     * @param communityQuery
     * @return
     */
    List<BaseCommunity> findUserCommunityList(BaseCommunity communityQuery);

    /**
     * 根据主键查找项目信息
     * @param communityId
     * @return
     */
    BaseCommunity get(String communityId);


    /**
     * 根据小区名称模糊查询小区列表
     * @return
     */
    List<BaseCommunity> findCommunityByLikeCommunityName(IRequest request, BaseCommunity community, int pageNum, int pageSize);

    /**
     * 根据经纬度查询附近的小区

     * @return
     */
    BaseCommunity findCommunityByLngLat(BaseCommunity community);

    /**
     * 分页查询小区--web端
     * @param community
     * @return
     */
    List<BaseCommunity> selectCommunity(IRequest request, BaseCommunity community, int pageNum, int pageSize);

    /**
     * 分页查询小区--客户端
     * @param community
     * @return
     */
    List<BaseCommunity> findList(IRequest request, BaseCommunity community, int pageNum, int pageSize);

    /**
     * 根据项目编码查询小区
     * @param community
     * @return
     */
    List<BaseCommunity> findListByCommunityCode(BaseCommunity community);

    /**
     * 根据项目编码查询小区
     * @param communityCode
     * @return
     */
    BaseCommunity findListByCommunityCode(String communityCode);

	/**
	 * 小区同步到功能配置
	 * @param community
	 */
	void appConfigCommunity(BaseCommunity community);


    void deleteCommunity(IRequest requestContext, String id) throws CustomerInfoException;
}