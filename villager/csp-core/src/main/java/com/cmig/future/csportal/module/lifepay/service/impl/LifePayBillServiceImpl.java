package com.cmig.future.csportal.module.lifepay.service.impl;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.lifepay.dto.LifePayBill;
import com.cmig.future.csportal.module.lifepay.mapper.LifePayBillMapper;
import com.cmig.future.csportal.module.lifepay.service.ILifePayBillService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LifePayBillServiceImpl extends BaseServiceImpl<LifePayBill> implements ILifePayBillService{

	@Autowired
	private LifePayBillMapper lifePayBillMapper;

	@Override
	@Transactional(readOnly = false)
	public void save(LifePayBill lifePayBill) {
		if(!StringUtils.isEmpty(lifePayBill.getId())){
			lifePayBillMapper.updateByPrimaryKeySelective(lifePayBill);
		}else{
			super.mapper.insertSelective(lifePayBill);
		}
	}

}