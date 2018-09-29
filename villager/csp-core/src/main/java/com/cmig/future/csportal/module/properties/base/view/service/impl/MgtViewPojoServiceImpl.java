package com.cmig.future.csportal.module.properties.base.view.service.impl;

import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewPojo;
import com.cmig.future.csportal.module.properties.base.view.mapper.MgtViewPojoMapper;
import com.cmig.future.csportal.module.properties.base.view.service.IMgtViewPojoService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MgtViewPojoServiceImpl extends BaseServiceImpl<MgtViewPojo> implements IMgtViewPojoService {

    @Autowired
    private MgtViewPojoMapper mgtViewPojoMapper;

    @Override
    public List<MgtViewPojo> findList(IRequest request, MgtViewPojo dto, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return mgtViewPojoMapper.findList(dto);
    }

    @Override
    public List<MgtViewPojo> findList(MgtViewPojo dto) {
        return mgtViewPojoMapper.findList(dto);
    }

}