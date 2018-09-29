package com.cmig.future.csportal.module.sys.apilog.service;

import com.cmig.future.csportal.module.sys.apilog.dto.SysLog;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface ISysLogService extends IBaseService<SysLog>, ProxySelf<ISysLogService>{

    /**
     * 删除1个月前日志定时任务
     */
    void excuteDeleteLogDataJob();
}