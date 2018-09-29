package com.cmig.future.csportal.module.properties.base.structure.mapper;

import com.cmig.future.csportal.module.properties.base.structure.dto.MgtStructure;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MgtStructureMapper extends Mapper<MgtStructure> {

    MgtStructure findByStructureCode(MgtStructure dto);

    int getCountByStructureCode(@Param(value = "structureCode") String structureCode);

    MgtStructure queryByStructureId(MgtStructure dto);

    int deleteByVersionId(MgtStructure dto);
}