package com.cmig.future.csportal.module.properties.base.map.service.impl;

import com.cmig.future.csportal.module.properties.base.map.dto.StructureBuildingMap;
import com.cmig.future.csportal.module.properties.base.map.mapper.StructureBuildingMapMapper;
import com.cmig.future.csportal.module.properties.base.map.service.IStructureBuildingMapService;
import com.cmig.future.csportal.module.properties.base.utils.MgtStructureHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StructureBuildingMapServiceImpl extends BaseServiceImpl<StructureBuildingMap> implements IStructureBuildingMapService {

    @Autowired
    private StructureBuildingMapMapper structureBuildingMapMapper;
    private static Logger logger = LoggerFactory.getLogger(StructureBuildingMapServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public StructureBuildingMap saveOrUpdate(IRequest requestCtx, StructureBuildingMap dto){
        logger.info("dto对象是:{}", JSONUtils.valueToString(dto));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(dto));
        }
        String status = dto.get__status();
        dto.setBuildingType(MgtStructureHelper.HOUSE);
        if (DTOStatus.ADD.equals(status)) {
            structureBuildingMapMapper.insertSelective(dto);
        } else if (DTOStatus.UPDATE.equals(status)) {
            structureBuildingMapMapper.updateByPrimaryKeySelective(dto);
        }
        return dto;
    }

    @Override
    public StructureBuildingMap findByBuildingIdAndStructureId(StructureBuildingMap dto){
        return structureBuildingMapMapper.findByBuildingIdAndStructureId(dto);
    }

    @Override
    public int deleteByStructureVersionId(StructureBuildingMap dto){
        return structureBuildingMapMapper.deleteByStructureVersionId(dto);
    }
}