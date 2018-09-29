package com.cmig.future.csportal.module.properties.base.customer.mapper;

import com.cmig.future.csportal.module.properties.base.customer.dto.BpGeneral;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BpGeneralMapper extends Mapper<BpGeneral>{

    int getByIdCard(BpGeneral general);

    List<BpGeneral> getByMobile(String mobile);
    
    Long getNextIndex();

	BpGeneral getUnionMobile(@Param(value = "mobile") String mobile);

	BpGeneral getByIdNo(@Param(value = "idNo") String idNo, @Param(value = "idType") String idType);

    void deleteByBpId(@Param(value = "bpId") Long bpId);

    List<BpGeneral> getByUserId(String userId);
}