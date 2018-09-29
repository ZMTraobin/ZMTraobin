package com.cmig.future.csportal.module.properties.base.customer.service.impl;

import com.cmig.future.csportal.module.properties.base.customer.dto.BpGeneral;
import com.cmig.future.csportal.module.properties.base.customer.service.IBpGeneralService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BpGeneralServiceImpl extends BaseServiceImpl<BpGeneral> implements IBpGeneralService {

}