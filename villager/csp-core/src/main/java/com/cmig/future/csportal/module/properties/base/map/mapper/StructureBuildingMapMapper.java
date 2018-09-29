package com.cmig.future.csportal.module.properties.base.map.mapper;

import com.cmig.future.csportal.module.properties.base.map.dto.StructureBuildingMap;
import com.hand.hap.mybatis.common.Mapper;

public interface StructureBuildingMapMapper extends Mapper<StructureBuildingMap> {

    StructureBuildingMap findByBuildingIdAndStructureId(StructureBuildingMap dto);

    int deleteByStructureVersionId(StructureBuildingMap dto);
}