package com.cmig.future.csportal.module.properties.base.view.service;


import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewPojo;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IMgtViewPojoService extends IBaseService<MgtViewPojo>, ProxySelf<IMgtViewPojoService> {

    List<MgtViewPojo> findList(IRequest request, MgtViewPojo dto, int pageNum, int pageSize);

    List<MgtViewPojo> findList(MgtViewPojo dto);

}