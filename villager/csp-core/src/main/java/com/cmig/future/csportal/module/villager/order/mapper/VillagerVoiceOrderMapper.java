package com.cmig.future.csportal.module.villager.order.mapper;

import com.cmig.future.csportal.module.villager.order.dto.VillagerVoiceOrder;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VillagerVoiceOrderMapper extends Mapper<VillagerVoiceOrder>{

    List<VillagerVoiceOrder> findList(VillagerVoiceOrder dto);

    List<VillagerVoiceOrder> findAdminList(VillagerVoiceOrder dto);

    void confirmReceipt(@Param("appUserId") String appUserId, @Param("id") Long id);

    void received(@Param("mgtUserId") String mgtUserId, @Param("id") Long id);
}