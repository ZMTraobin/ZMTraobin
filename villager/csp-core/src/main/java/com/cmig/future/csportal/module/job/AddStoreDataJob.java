package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.search.SearchApiInvoking;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;

public class AddStoreDataJob extends AbstractJob{

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// 获取商户数据
		String storeJsonString=SearchApiInvoking.getData("store");
	    // 添加商户数据
		SearchApiInvoking.apiInvoking(SearchApiInvoking.getStoreUrl(), storeJsonString);
		
	}

}
