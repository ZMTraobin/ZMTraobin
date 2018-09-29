package com.cmig.future.csportal.module.villager.wealth.service;

import java.util.Map;

import com.hand.hap.core.ProxySelf;

public interface IWealthService extends ProxySelf<IWealthService>{

	Map calculate(Long totalIncome, Long input);


}