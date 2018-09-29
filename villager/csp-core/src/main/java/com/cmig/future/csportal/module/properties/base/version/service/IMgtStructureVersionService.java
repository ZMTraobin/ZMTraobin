package com.cmig.future.csportal.module.properties.base.version.service;

import com.cmig.future.csportal.module.properties.base.version.dto.MgtStructureVersion;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IMgtStructureVersionService extends IBaseService<MgtStructureVersion>, ProxySelf<IMgtStructureVersionService>{

    List<MgtStructureVersion> findList(IRequest request, MgtStructureVersion mgtStructureVersion, int pageNum, int pageSize);

    List<MgtStructureVersion> saveOrUpdate(IRequest requestCtx, List<MgtStructureVersion> dto);

    MgtStructureVersion queryByVersionId(MgtStructureVersion dto);

    int versionDel(MgtStructureVersion dto);

    //查询该建筑接口是否存在默认的
    List<MgtStructureVersion> findByCommunityIdAndDefault(MgtStructureVersion dto);

}