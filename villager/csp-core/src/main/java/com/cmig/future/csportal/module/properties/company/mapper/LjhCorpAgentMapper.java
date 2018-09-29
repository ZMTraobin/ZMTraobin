package com.cmig.future.csportal.module.properties.company.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.properties.company.dto.LjhCorpAgent;

import java.util.List;

public interface LjhCorpAgentMapper extends Mapper<LjhCorpAgent>{

	List<LjhCorpAgent> findAgent(LjhCorpAgent ljhCorpAgent);

}