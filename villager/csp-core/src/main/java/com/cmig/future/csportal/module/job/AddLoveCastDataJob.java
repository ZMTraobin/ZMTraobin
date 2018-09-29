package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.search.SearchApiInvoking;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;

public class AddLoveCastDataJob extends AbstractJob{

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// 获取爱投数据
		String itJsonString=SearchApiInvoking.getData("");
		// 添加爱投数据
		SearchApiInvoking.apiInvoking(SearchApiInvoking.getIt_url(), itJsonString);
		
	}

}
