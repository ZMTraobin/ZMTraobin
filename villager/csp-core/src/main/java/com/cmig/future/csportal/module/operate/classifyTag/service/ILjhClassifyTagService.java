package com.cmig.future.csportal.module.operate.classifyTag.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.operate.classifyTag.dto.LjhClassifyTag;

public interface ILjhClassifyTagService extends IBaseService<LjhClassifyTag>, ProxySelf<ILjhClassifyTagService> {

    List<LjhClassifyTag> saveOrUpdate(IRequest requestCtx, List<LjhClassifyTag> dto);

    List<LjhClassifyTag> select(IRequest requestCtx, LjhClassifyTag dto, int page, int pageSize);

    List<LjhClassifyTag> findList(LjhClassifyTag baseClassifyTag);

}