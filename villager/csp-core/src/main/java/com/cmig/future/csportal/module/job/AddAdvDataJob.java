package com.cmig.future.csportal.module.job;

import com.alibaba.fastjson.JSON;
import com.cmig.future.csportal.module.adv.dto.AppAdv;
import com.cmig.future.csportal.module.adv.mapper.AppAdvMapper;
import com.cmig.future.csportal.module.search.SearchApiInvoking;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AddAdvDataJob extends AbstractJob{
    
	@Autowired AppAdvMapper appAdvMapper;
	
	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// 获取广告数据
		List<AppAdv> list=appAdvMapper.selecteAdv();
		String jsonstr = JSON.toJSONString(list);
		//添加广告数据
		SearchApiInvoking.apiInvoking(SearchApiInvoking.getAd_url(), jsonstr);
		
	}

}
