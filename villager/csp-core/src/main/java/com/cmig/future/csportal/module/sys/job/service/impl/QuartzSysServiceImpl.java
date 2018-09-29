package com.cmig.future.csportal.module.sys.job.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmig.future.csportal.module.sys.job.mapper.JobSysDetailMapper;
import com.cmig.future.csportal.module.sys.job.service.ISysQuartzService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.cmig.future.csportal.module.sys.job.dto.JobDetailDto;
import com.hand.hap.job.dto.JobData;
import com.hand.hap.job.dto.JobInfoDetailDto;

@Service
public class QuartzSysServiceImpl implements ISysQuartzService {
    
    private final Logger logger = LoggerFactory.getLogger(QuartzSysServiceImpl.class);
    
    @Autowired
    private JobSysDetailMapper jobSysDetailMapper;
    @Autowired
    private Scheduler quartzScheduler;

    @Override
    public List<JobInfoDetailDto> getJobInfoDetails(IRequest request, JobDetailDto example, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        logger.info("pagesize:"+page+",,,"+pagesize);
        //example.setPage(page);
        //example.setPagesize(pagesize);
        List<JobInfoDetailDto> selectJobInfoDetails = jobSysDetailMapper.selectJobInfoDetails(example);

        for (JobInfoDetailDto jobInfoDetailDto : selectJobInfoDetails) {
            try {
                JobKey jobKey = new JobKey(jobInfoDetailDto.getJobName(), jobInfoDetailDto.getJobGroup());
                JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                String[] keys = jobDataMap.getKeys();
                List<JobData> jobDatas = new ArrayList<JobData>();
                for (String string : keys) {
                    JobData e = new JobData();
                    e.setName(string);
                    e.setValue(jobDataMap.getString(string));
                    jobDatas.add(e);
                }

                Trigger trigger = quartzScheduler.getTriggersOfJob(jobKey).get(0);
                if (trigger instanceof SimpleTrigger) {
                    jobInfoDetailDto.setTriggerType("SIMPLE");
                    jobInfoDetailDto.setRepeatCount(((SimpleTrigger) trigger).getRepeatCount());
                    jobInfoDetailDto.setRepeatInterval(((SimpleTrigger) trigger).getRepeatInterval());
                } else if (trigger instanceof CronTrigger) {
                    jobInfoDetailDto.setCronExpression(((CronTrigger) trigger).getCronExpression());
                    jobInfoDetailDto.setTriggerType("CRON");
                }
                jobInfoDetailDto.setTriggerName(trigger.getKey().getName());
                jobInfoDetailDto.setTriggerGroup(trigger.getKey().getGroup());
                jobInfoDetailDto.setStartTime(trigger.getStartTime());
                jobInfoDetailDto.setEndTime(trigger.getEndTime());

                jobInfoDetailDto.setJobDatas(jobDatas);

                // get running state of job
                Trigger.TriggerState ts = quartzScheduler.getTriggerState(trigger.getKey());
                jobInfoDetailDto.setRunningState(ts.name());
            } catch (SchedulerException e) {
                jobInfoDetailDto.setRunningState(Trigger.TriggerState.ERROR.name());
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return selectJobInfoDetails;
    
    }


}