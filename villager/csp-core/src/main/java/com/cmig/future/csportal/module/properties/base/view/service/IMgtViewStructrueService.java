package com.cmig.future.csportal.module.properties.base.view.service;


import com.cmig.future.csportal.module.properties.base.view.dto.MgtViewStructure;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IMgtViewStructrueService extends IBaseService<MgtViewStructure>, ProxySelf<IMgtViewStructrueService> {

    List<MgtViewStructure> findList(IRequest request, MgtViewStructure dto, int pageNum, int pageSize);

    List<MgtViewStructure> findList(MgtViewStructure dto);

}