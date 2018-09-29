package com.cmig.future.csportal.module.properties.company.service;

import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;

import java.util.List;

public interface ILjhCorpAgentService extends IBaseService<LjhCorpAgent>, ProxySelf<ILjhCorpAgentService>{

	List<LjhCorpAgent> findAgent(LjhCorpAgent ljhCorpAgent);

}