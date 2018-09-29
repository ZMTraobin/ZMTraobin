package com.cmig.future.csportal.module.properties.base.contacter.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.properties.base.contacter.dto.BpOwnerContacter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BpOwnerContacterMapper extends Mapper<BpOwnerContacter>{

    List<BpOwnerContacter> getContacterList(@Param("userId") String userId, @Param("communityId") String communityId);

    BpOwnerContacter selectById(@Param("userId") String userId, @Param("ownerContacterId") Long ownerContacterId,@Param("communityId") String communityId);

    Integer checkUnique( @Param("ownerId") Long ownerId, @Param("mobile") String mobile );
}