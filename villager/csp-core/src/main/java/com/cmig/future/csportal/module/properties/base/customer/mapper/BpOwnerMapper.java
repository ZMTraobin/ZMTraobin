package com.cmig.future.csportal.module.properties.base.customer.mapper;

import com.cmig.future.csportal.module.properties.base.customer.dto.BpOwner;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BpOwnerMapper extends Mapper<BpOwner>{

    List<BpOwner> selectCustomer(@Param(value = "bpId") Long bpId);

    Long getOwnerIdByIdNo(@Param(value = "idNo") String idNo);

    List<BpOwner> findGeneralByBuildingId(@Param(value = "buildingId") Long buildingId);

    List<BpOwner> queryOwners(BpOwner owner);

    List<BpOwner> getByUserId(String userId);
}