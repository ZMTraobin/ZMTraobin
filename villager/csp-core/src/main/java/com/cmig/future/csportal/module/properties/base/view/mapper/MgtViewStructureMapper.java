package com.cmig.future.csportal.module.properties.base.view.mapper;

import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewStructure;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface MgtViewStructureMapper extends Mapper<MgtViewStructure> {

    List<MgtViewStructure> findList(MgtViewStructure dto);

}