package com.cmig.future.csportal.module.villager.wealth.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.villager.wealth.service.IWealthService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WealthServiceImpl implements IWealthService{

	@Override
	public Map calculate(Long totalIncome, Long input) {
		// TODO Auto-generated method stub
		Map map = new HashMap<>();
		int year = Integer.parseInt(DateUtils.formatDate(new Date(), "yyyy"));
        // 年度数组
        int years[] = {year+1,year+2,year+3,year+4,year+5};
        // 存款数组
        Long deposits[] = new Long[5];
        // 收入数组
        Long totalincomes[] = new Long[5];
        // 资产数组
        Long assets[] = new Long[5];
        
        // 第1年
        // 存款=净收入
        deposits[0]= totalIncome - input;
        // 收入 = 总收入
        totalincomes[0] = totalIncome;
        // 资产 = 收入+存款
        assets[0] = deposits[0] + totalincomes[0];
        // 第2年
        // 存款=当年净收入+上年存款
        deposits[1]= deposits[0] + totalIncome - input;
        // 收入 = 当年总收入+ 上年总收入
        totalincomes[1] =totalincomes[0] + totalIncome;
        // 资产 = 收入+存款
        assets[1] = deposits[1] + totalincomes[1];
        // 第3年
        // 存款=当年净收入+上年存款
        deposits[2]= deposits[1] + totalIncome - input;
        // 收入 = 当年总收入+ 上年总收入
        totalincomes[2] =totalincomes[1] + totalIncome;
        // 资产 = 收入+存款
        assets[2] = deposits[2] + totalincomes[2];
        // 第4年
        // 存款=当年净收入+上年存款
        deposits[3]= deposits[2] + totalIncome - input;
        // 收入 = 当年总收入+ 上年总收入
        totalincomes[3] =totalincomes[2] + totalIncome;
        // 资产 = 收入+存款
        assets[3] = deposits[3] + totalincomes[3];
        // 第5年
        // 存款=当年总收入+上年存款
        deposits[4]= deposits[3] + totalIncome;
        // 收入 = 当年总收入+ 上年总收入
        totalincomes[4] =totalincomes[3] + totalIncome;
        // 资产 = 收入+存款
        assets[4] = deposits[4] + totalincomes[4];
		
        // 增长倍数
        Long times = deposits[4] / deposits[0];
        
		
		map.put("years", years);
        map.put("deposits", deposits);
        map.put("totalincomes", totalincomes);
        map.put("assets", assets);
        map.put("times", times);
		return map;
	}


}