package com.cmig.future.csportal.module.properties.base.view.mapper;

import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewPojo;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface MgtViewPojoMapper extends Mapper<MgtViewPojo> {

    List<MgtViewPojo> findList(MgtViewPojo dto);

}