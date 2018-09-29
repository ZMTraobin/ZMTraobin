package com.cmig.future.csportal.module.properties.base.version.mapper;

import com.cmig.future.csportal.module.properties.base.version.dto.MgtStructureVersion;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface MgtStructureVersionMapper extends Mapper<MgtStructureVersion> {

    List<MgtStructureVersion> findList(MgtStructureVersion mgtStructureVersion);

    MgtStructureVersion queryByVersionId(MgtStructureVersion dto);

    int versionDel(MgtStructureVersion dto);

    //查询该建筑接口是否存在默认的
    List<MgtStructureVersion> findByCommunityIdAndDefault(MgtStructureVersion dto);

}