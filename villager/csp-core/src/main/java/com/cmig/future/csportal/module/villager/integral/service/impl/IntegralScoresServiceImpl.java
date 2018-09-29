package com.cmig.future.csportal.module.villager.integral.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.cmig.future.csportal.module.villager.integral.dto.IntegralScores;
import com.cmig.future.csportal.module.villager.integral.service.IIntegralScoresService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntegralScoresServiceImpl extends BaseServiceImpl<IntegralScores> implements IIntegralScoresService{

}