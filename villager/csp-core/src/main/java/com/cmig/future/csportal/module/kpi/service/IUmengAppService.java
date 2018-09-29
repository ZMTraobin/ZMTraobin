package com.cmig.future.csportal.module.kpi.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.kpi.dto.UmengApp;

public interface IUmengAppService extends IBaseService<UmengApp>, ProxySelf<IUmengAppService>{

	void synApps() throws Exception;

}