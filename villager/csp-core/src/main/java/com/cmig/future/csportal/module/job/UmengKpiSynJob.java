package com.cmig.future.csportal.module.job;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.kpi.UmengKpiHelper;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiDurationService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiEventGroupService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiResultService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiUserService;
import com.cmig.future.csportal.module.kpi.service.IUmengAppService;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:19 2018/1/25.
 * @Modified by zhangtao on 11:19 2018/1/25.
 */
public class UmengKpiSynJob extends AbstractJob {
	private static final Logger logger= LoggerFactory.getLogger(UmengKpiSynJob.class);

	@Autowired
	private IUmengAppService umengAppService;

	@Autowired
	private ILjhKpiUserService ljhKpiUserService;

	@Autowired
	private ILjhKpiDurationService ljhKpiDurationService;

	@Autowired
	private ILjhKpiEventGroupService ljhKpiEventGroupService;

	@Autowired
	private ILjhKpiResultService ljhKpiResultService;

	@Override
	public void safeExecute(JobExecutionContext jobExecutionContext) throws Exception {
		logger.debug("同步友盟kpi任务开始...");
		umengAppService.synApps();
		String yestday=DateUtils.formatDate(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd");
		ljhKpiUserService.synUserKpi(yestday);
		ljhKpiDurationService.synDurationKpi(yestday, UmengKpiHelper.PERIOD_TYPE_DAILY);
		ljhKpiDurationService.synDurationKpi(yestday, UmengKpiHelper.PERIOD_TYPE_DAILY_PER_LAUNCH);
		ljhKpiEventGroupService.synEventGroupKpi(yestday);
		ljhKpiResultService.runResult(DateUtils.parseDate(yestday));
		logger.debug("同步友盟kpi任务结束...");
	}
}
