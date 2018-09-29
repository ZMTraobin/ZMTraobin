package com.cmig.future.csportal.module.properties.base.house.mapper;

import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MgtHouseMapper extends Mapper<MgtHouse> {

    MgtHouse findByStructureCode(MgtHouse dto);

    MgtHouse queryByHouseId(MgtHouse dto);

    int getCountByHouseCode(@Param(value = "houseCode") String houseCode);

    MgtHouse findBySourceHouseCodeAndCid(MgtHouse dto);

    MgtHouse findBySourceHouseCodeAndSourceSystem(MgtHouse dto);

    List<MgtHouse> selectList(MgtHouse dto);

}