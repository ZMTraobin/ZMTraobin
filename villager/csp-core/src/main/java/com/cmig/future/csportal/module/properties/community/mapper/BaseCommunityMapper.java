package com.cmig.future.csportal.module.properties.community.mapper;

import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * BaseCommunityMapper
 *
 * @author bubu
 *
 * 2017-3-21
 */
public interface BaseCommunityMapper extends Mapper<BaseCommunity>{

    int insertCommunity(BaseCommunity community);

    int updateCommunity(BaseCommunity community);

    BaseCommunity getBySourceSystemId(BaseCommunity community);

    BaseCommunity findByCommunityName(BaseCommunity community);

    List<BaseCommunity> findCommunityListByName(BaseCommunity community);

    int deleteCommunity(BaseCommunity community);

    List<BaseCommunity> findUserCommunityList(BaseCommunity communityQuery);

    List<BaseCommunity> findCommunityByLikeCommunityName(BaseCommunity community);

    BaseCommunity findCommunityByLngLat(BaseCommunity community);

    List<BaseCommunity> selectCommunity(BaseCommunity community);

    List<BaseCommunity> findList(BaseCommunity baseCommunity);

    BaseCommunity getById(BaseCommunity baseCommunity);

    List<BaseCommunity> findListByCommunityCode(BaseCommunity community);

    List<BaseCommunity> isBind(@Param(value = "id") String id);
    
    List<BaseCommunity> isDefault(@Param(value = "id") String id);
}