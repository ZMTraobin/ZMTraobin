package com.cmig.future.csportal.module.sys.logs.service.impl;

import com.cmig.future.csportal.module.sys.logs.mapper.LjhLoginLogMapper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.sys.logs.dto.LjhLoginLog;
import com.cmig.future.csportal.module.sys.logs.service.ILjhLoginLogService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LjhLoginLogServiceImpl extends BaseServiceImpl<LjhLoginLog> implements ILjhLoginLogService{

    @Autowired
    private LjhLoginLogMapper ljhLoginLogMapper;

    @Override
    @Transactional
    public void login(String logType, String userId, String token, String lastIp) {
        LjhLoginLog log=new LjhLoginLog();
        log.setToken(token);
        log.setLogType(logType);
        log.setUserId(userId);
        log.setIp(lastIp);
        ljhLoginLogMapper.insertSelective(log);
    }
}