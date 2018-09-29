package com.cmig.future.csportal.module.properties.base.view.service.impl;

import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewStructure;
import com.cmig.future.csportal.module.properties.base.view.mapper.MgtViewStructureMapper;
import com.cmig.future.csportal.module.properties.base.view.service.IMgtViewStructrueService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MgtViewStructureServiceImpl extends BaseServiceImpl<MgtViewStructure> implements IMgtViewStructrueService {

    @Autowired
    private MgtViewStructureMapper mgtViewStructureMapper;

    @Override
    public List<MgtViewStructure> findList(IRequest request, MgtViewStructure dto, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return mgtViewStructureMapper.findList(dto);
    }

    @Override
    public List<MgtViewStructure> findList(MgtViewStructure dto) {
        return mgtViewStructureMapper.findList(dto);
    }

}