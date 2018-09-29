package com.cmig.future.csportal.module.properties.payment.receivablecopy.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.dto.MgtReceivableCopy;

import java.util.List;
import java.util.Map;

public interface IMgtReceivableCopyService extends IBaseService<MgtReceivableCopy>, ProxySelf<IMgtReceivableCopyService>{

    List<MgtReceivableCopy> queryAll();

	void notifySuccess(Map map);
}