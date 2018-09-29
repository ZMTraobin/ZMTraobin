package com.cmig.future.csportal.module.properties.base.version.service.impl;

import com.cmig.future.csportal.module.properties.base.version.dto.MgtStructureVersion;
import com.cmig.future.csportal.module.properties.base.version.mapper.MgtStructureVersionMapper;
import com.cmig.future.csportal.module.properties.base.version.service.IMgtStructureVersionService;
import com.github.pagehelper.PageHelper;
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

import java.util.List;

@Service
@Transactional
public class MgtStructureVersionServiceImpl extends BaseServiceImpl<MgtStructureVersion> implements IMgtStructureVersionService {

    private final Logger logger = LoggerFactory.getLogger(MgtStructureVersionServiceImpl.class);

    @Autowired
    private MgtStructureVersionMapper mgtStructureVersionMapper;

    @Override
    public List<MgtStructureVersion> findList(IRequest request, MgtStructureVersion mgtStructureVersion, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        return mgtStructureVersionMapper.findList(mgtStructureVersion);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MgtStructureVersion> saveOrUpdate(IRequest requestCtx, List<MgtStructureVersion> dto){
        logger.info("dto对象是:{}", JSONUtils.valueToString(dto));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(dto));
        }
        if (dto == null || dto.isEmpty()) {
            return dto;
        }
        for (MgtStructureVersion version : dto) {
            String status = version.get__status();
            if (DTOStatus.ADD.equals(status)) {
                mgtStructureVersionMapper.insertSelective(version);
            } else if (DTOStatus.UPDATE.equals(status)) {
                mgtStructureVersionMapper.updateByPrimaryKeySelective(version);
            }
        }
        return dto;
    }

    @Override
    public MgtStructureVersion queryByVersionId(MgtStructureVersion dto){
        return mgtStructureVersionMapper.queryByVersionId(dto);
    }

    @Override
    public int versionDel(MgtStructureVersion dto){
        return mgtStructureVersionMapper.versionDel(dto);
    }
    //查询该建筑结构是否存在默认
    @Override
    public List<MgtStructureVersion> findByCommunityIdAndDefault(MgtStructureVersion dto) {
        return mgtStructureVersionMapper.findByCommunityIdAndDefault(dto);
    }

}