package com.cmig.future.csportal.module.sys.logs.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.sys.logs.dto.LjhLoginLog;

public interface ILjhLoginLogService extends IBaseService<LjhLoginLog>, ProxySelf<ILjhLoginLogService>{

    void login(String logType, String userId, String token, String lastIp);
}