package com.cmig.future.csportal.module.properties.company.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;
import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;
import com.cmig.future.csportal.module.properties.company.exception.CompanyException;
import com.cmig.future.csportal.module.properties.company.mapper.LjhBaseCompanyMapper;
import com.cmig.future.csportal.module.properties.company.mapper.LjhCorpAgentMapper;
import com.cmig.future.csportal.module.properties.company.service.ILjhBaseCompanyService;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LjhBaseCompanyServiceImpl extends BaseServiceImpl<LjhBaseCompany> implements ILjhBaseCompanyService {

    @Autowired
    private LjhBaseCompanyMapper ljhBaseCompanyMapper;
    @Autowired
    private LjhCorpAgentMapper ljhCorpAgentMapper;

    @Override
    public List<LjhBaseCompany> findCorpList() {
        return ljhBaseCompanyMapper.findCorpList();
    }

    @Override
    public LjhBaseCompany getBySourceSystemId(LjhBaseCompany companyQuery) {
        return ljhBaseCompanyMapper.getBySourceSystemId(companyQuery);
    }

    @Override
    public void save(LjhBaseCompany company) {
        if (!StringUtils.isEmpty(company.getCompanyId())) {
            ljhBaseCompanyMapper.updateByPrimaryKeySelective(company);
        } else {
            company.setCompanyId(IdGen.uuid());
            ljhBaseCompanyMapper.insertSelective(company);
        }
    }

    @Override
    public List<LjhBaseCompany> updateCompany(IRequest requestCtx, List<LjhBaseCompany> dto) throws CompanyException {
        if (CollectionUtils.isNotEmpty(dto)) {
            LjhBaseCompany baseCompany = dto.get(0);
            String companyId = baseCompany.getCompanyId();
            if (companyId != null) {
                //handleCorpId(baseCompany);
                ljhBaseCompanyMapper.updateByPrimaryKey(baseCompany);
                saveCorpAgents(baseCompany);
            } else {
                //新增
                baseCompany.setCompanyId(IdGen.uuid());
                ljhBaseCompanyMapper.insertSelective(baseCompany);
                saveCorpAgents(baseCompany);
            }
	        WorkWxHelper.cleanCorpInfo();
        }
        return dto;
    }

    private void handleCorpId(LjhBaseCompany baseCompany) throws CompanyException {
        //1.如果原先有值，则不能更新为空,顺便要更新行表
        LjhBaseCompany company = ljhBaseCompanyMapper.selectByPrimaryKey(baseCompany.getCompanyId());
        String oldCorpId = company.getCorpId();
        String newCorpId = baseCompany.getCorpId();
        System.out.println("old:"+oldCorpId+"   new:"+newCorpId);
        if(StringUtils.isNotEmpty(oldCorpId) && StringUtils.isEmpty(newCorpId)){
            throw new CompanyException(CompanyException.MSG_ERROR_CORPID_NOT_NULL, "corpId不能为空", null);
        }
        if(StringUtils.isNotEmpty(oldCorpId) && !oldCorpId.equals(newCorpId)){
            //更新行表
            LjhCorpAgent oldAgent = new LjhCorpAgent();
            oldAgent.setCorpId(oldCorpId);
            List<LjhCorpAgent> agentList = ljhCorpAgentMapper.findAgent(oldAgent);
            if(CollectionUtils.isNotEmpty(agentList)){
                for (LjhCorpAgent agent:agentList){
                    agent.setCorpId(newCorpId);
                    ljhCorpAgentMapper.updateByPrimaryKey(agent);
                }
            }
        }
    }

    private void saveCorpAgents(LjhBaseCompany baseCompany) throws CompanyException {
        List<LjhCorpAgent> corpAgents = baseCompany.getCorpAgents();
        if (CollectionUtils.isNotEmpty(corpAgents)) {
            for (LjhCorpAgent agent : corpAgents) {
                agent.setCorpId(baseCompany.getCompanyId());
                if (agent.getId() != null) {
                    ljhCorpAgentMapper.updateByPrimaryKey(agent);
                } else {
	                agent.setAgentNo(StringUtils.getRandomNum(8));
                    ljhCorpAgentMapper.insertSelective(agent);
                }
            }
        }
    }

    @Override
    public List<LjhBaseCompany> selectByCondition(IRequest requestContext, LjhBaseCompany dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ljhBaseCompanyMapper.selectByCondition(dto);
    }

    @Override
    public int batchDelete(List<LjhBaseCompany> list) {
        WorkWxHelper.cleanCorpInfo();
        return super.batchDelete(list);
    }
}