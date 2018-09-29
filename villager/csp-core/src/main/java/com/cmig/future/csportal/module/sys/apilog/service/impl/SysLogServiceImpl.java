package com.cmig.future.csportal.module.sys.apilog.service.impl;

import com.cmig.future.csportal.module.sys.apilog.dto.SysLog;
import com.cmig.future.csportal.module.sys.apilog.mapper.SysLogMapper;
import com.cmig.future.csportal.module.sys.apilog.service.ISysLogService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysLogServiceImpl extends BaseServiceImpl<SysLog> implements ISysLogService{

    @Autowired
    SysLogMapper sysLogMapper;

    @Override
    public void excuteDeleteLogDataJob() {
        sysLogMapper.deleteSysLogBeforeOneMonth();
    }
}