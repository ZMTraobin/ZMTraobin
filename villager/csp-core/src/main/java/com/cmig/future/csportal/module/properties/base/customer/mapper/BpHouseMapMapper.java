package com.cmig.future.csportal.module.properties.base.customer.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.payment.receivable.dto.MgtReceivableDetail;
import com.hand.hap.mybatis.common.Mapper;

public interface BpHouseMapMapper extends Mapper<BpHouseMap>{

    BpHouseMap identifyHouse(@Param(value = "mobile") String mobile, @Param(value = "buildingId") Long buildingId);

    List<BpHouseMap> queryByBpId(@Param(value = "bpId") Long bpId);

    //void deleteByOwnerId(@Param(value = "bpOwnerId") Long bpOwnerId);

    void deleteByExtId(@Param(value = "bpExtId") Long bpExtId);

    List<BpHouseMap> queryByUserId(@Param(value = "userId") String userId, @Param(value = "communityId") String communityId, @Param(value = "sysDate") Date sysDate);

    String queryOwnerName(@Param(value = "buildingId") Long buildingId, @Param(value = "sysDate") Date sysDate);

    List<MgtReceivableDetail> queryBillsFirstType(MgtReceivableDetail detail);

    List<MgtReceivableDetail> queryBills(MgtReceivableDetail detail);

    Long queryUnCalledFee(MgtReceivableDetail detail);

    Long queryPayableFee(MgtReceivableDetail detail);

    MgtReceivableDetail queryPeriod(MgtReceivableDetail detail);

    String getHouseType(@Param(value = "userId") String userId, @Param(value = "buildingId") Long buildingId);

    List<MgtReceivableDetail> queryByBuildingId( @Param(value = "communityId") String communityId, @Param(value = "appUserId") String appUserId);

    List<MgtReceivableDetail> queryByBuildingIdOwner(@Param(value = "buildingId") Long buildingId);

    int getByExtBuilding(@Param(value = "bpExtId") Long bpExtId, @Param(value = "buildingId") Long buildingId);

    List<BpHouseMap> getByBpBuilding(@Param(value = "bpId") Long bpId, @Param(value = "buildingId") Long buildingId);

    List<BpHouseMap> getOwnerHouse(@Param(value = "userId") String userId, @Param(value = "communityId") String communityId);

    void deleteContacter(@Param("bpExtId") Long bpExtId, @Param("communityId") String communityId);

    Long queryCommunityBills(@Param(value = "communityId") String communityId,@Param(value = "userId") String userId );

    void lossValid(@Param(value = "buildingId") Long buildingId );

    List<BpHouseMap> queryValidData();

    void valiadByMapId( Long mapId );
}