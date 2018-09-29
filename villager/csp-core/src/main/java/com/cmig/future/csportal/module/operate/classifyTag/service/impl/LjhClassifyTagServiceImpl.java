package com.cmig.future.csportal.module.operate.classifyTag.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.operate.classifyTag.dto.LjhClassifyTag;
import com.cmig.future.csportal.module.operate.classifyTag.mapper.LjhClassifyTagMapper;
import com.cmig.future.csportal.module.operate.classifyTag.service.ILjhClassifyTagService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import net.sf.json.util.JSONUtils;

@Service
@Transactional
public class LjhClassifyTagServiceImpl extends BaseServiceImpl<LjhClassifyTag> implements ILjhClassifyTagService {

    private final Logger logger = LoggerFactory.getLogger(LjhClassifyTagServiceImpl.class);

    @Autowired
    private LjhClassifyTagMapper ljhClassifyTagMapper;

    @Override
    public List<LjhClassifyTag> saveOrUpdate(IRequest requestCtx, List<LjhClassifyTag> dto) {
        // 处理文章内容
        logger.info("dto对象是:{}", JSONUtils.valueToString(dto));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(dto));
        }
        if (dto == null || dto.isEmpty()) {
            return dto;
        }
        for (LjhClassifyTag tag : dto) {
            String status = tag.get__status();
            if (DTOStatus.ADD.equals(status)) {
                String id = IdGen.uuid();
                tag.setId(id);
                ljhClassifyTagMapper.insertSelective(tag);
            } else if (DTOStatus.UPDATE.equals(status)) {
                ljhClassifyTagMapper.updateByPrimaryKeySelective(tag);
            }
        }
        return dto;
    }

    @Override
    public List<LjhClassifyTag> select(IRequest requestCtx, LjhClassifyTag dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ljhClassifyTagMapper.select(dto);
    }

    @Override
    public List<LjhClassifyTag> findList(LjhClassifyTag baseClassifyTag) {
        return ljhClassifyTagMapper.select(baseClassifyTag);
    }

}