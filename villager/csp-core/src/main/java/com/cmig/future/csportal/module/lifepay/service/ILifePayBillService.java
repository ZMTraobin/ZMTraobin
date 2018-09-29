package com.cmig.future.csportal.module.lifepay.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import com.cmig.future.csportal.module.lifepay.dto.LifePayBill;

public interface ILifePayBillService extends IBaseService<LifePayBill>, ProxySelf<ILifePayBillService>{

	void save(LifePayBill lifePayBill);
}