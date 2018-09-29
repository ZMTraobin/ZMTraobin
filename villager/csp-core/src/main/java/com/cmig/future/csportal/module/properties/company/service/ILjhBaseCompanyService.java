package com.cmig.future.csportal.module.properties.company.service;

import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;
import com.cmig.future.csportal.module.properties.company.exception.CompanyException;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;

import java.util.List;

public interface ILjhBaseCompanyService extends IBaseService<LjhBaseCompany>, ProxySelf<ILjhBaseCompanyService>{

	List<LjhBaseCompany> findCorpList();

	LjhBaseCompany getBySourceSystemId(LjhBaseCompany companyQuery);

	void save(LjhBaseCompany companyQuery);

	List<LjhBaseCompany> updateCompany(IRequest requestCtx, List<LjhBaseCompany> dto) throws CompanyException;

	List<LjhBaseCompany> selectByCondition(IRequest requestContext, LjhBaseCompany dto, int page, int pageSize);

}