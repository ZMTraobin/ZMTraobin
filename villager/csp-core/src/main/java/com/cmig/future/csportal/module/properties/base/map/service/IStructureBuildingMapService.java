package com.cmig.future.csportal.module.properties.base.map.service;

import com.cmig.future.csportal.module.properties.base.map.dto.StructureBuildingMap;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;


public interface IStructureBuildingMapService extends IBaseService<StructureBuildingMap>, ProxySelf<IStructureBuildingMapService>{

    StructureBuildingMap saveOrUpdate(IRequest requestCtx, StructureBuildingMap dto);

    StructureBuildingMap findByBuildingIdAndStructureId(StructureBuildingMap dto);

    int deleteByStructureVersionId(StructureBuildingMap dto);
}