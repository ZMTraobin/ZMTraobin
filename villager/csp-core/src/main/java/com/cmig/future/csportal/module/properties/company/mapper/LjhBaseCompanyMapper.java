package com.cmig.future.csportal.module.properties.company.mapper;

import com.hand.hap.mybatis.common.Mapper;
import com.cmig.future.csportal.module.properties.company.dto.LjhBaseCompany;

import java.util.List;

public interface LjhBaseCompanyMapper extends Mapper<LjhBaseCompany>{

	List<LjhBaseCompany> findCorpList();

	LjhBaseCompany getBySourceSystemId(LjhBaseCompany companyQuery);

    List<LjhBaseCompany> selectByCondition(LjhBaseCompany dto);
}