package com.cmig.future.csportal.module.sys.appconfig.mapper;

import com.cmig.future.csportal.module.sys.appconfig.dto.AppCommunityFunction;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppCommunityFunctionMapper extends Mapper<AppCommunityFunction>{

	@Override
	List<AppCommunityFunction> select(AppCommunityFunction appCommunityFunction);

	void batchDeleteByCid(@Param(value = "appConfigCommunityIds") String[] appConfigCommunityIds);
}