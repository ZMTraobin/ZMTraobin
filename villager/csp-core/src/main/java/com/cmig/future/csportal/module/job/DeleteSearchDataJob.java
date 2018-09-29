package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.module.search.SearchApiInvoking;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;

public class DeleteSearchDataJob extends AbstractJob{

	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// 清空搜索数据
		SearchApiInvoking.apiInvoking(SearchApiInvoking.getDelete_Url(), "");
		
	}

}
