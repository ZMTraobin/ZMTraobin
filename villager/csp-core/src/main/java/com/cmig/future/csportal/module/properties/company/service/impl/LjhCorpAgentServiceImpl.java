package com.cmig.future.csportal.module.properties.company.service.impl;

import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;
import com.cmig.future.csportal.module.properties.company.mapper.LjhBaseCompanyMapper;
import com.cmig.future.csportal.module.properties.company.mapper.LjhCorpAgentMapper;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;
import com.cmig.future.csportal.module.properties.company.service.ILjhCorpAgentService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LjhCorpAgentServiceImpl extends BaseServiceImpl<LjhCorpAgent> implements ILjhCorpAgentService {

    @Autowired
    private LjhCorpAgentMapper ljhCorpAgentMapper;
    @Autowired
    private LjhBaseCompanyMapper ljhBaseCompanyMapper;

    @Override
    public List<LjhCorpAgent> findAgent(LjhCorpAgent ljhCorpAgent) {
        return ljhCorpAgentMapper.findAgent(ljhCorpAgent);
    }

    @Override
    public int batchDelete(List<LjhCorpAgent> list) {
        WorkWxHelper.cleanCorpInfo();
        return super.batchDelete(list);
    }
}