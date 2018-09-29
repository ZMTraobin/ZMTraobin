package com.cmig.future.csportal.module.sys.apilog.mapper;

import com.cmig.future.csportal.module.sys.apilog.dto.SysLog;
import com.hand.hap.mybatis.common.Mapper;

public interface SysLogMapper extends Mapper<SysLog>{

    int deleteSysLogBeforeOneMonth();
}