package com.cmig.future.csportal.module.kpi.service.impl;

import com.cmig.future.csportal.common.utils.Arith;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.kpi.UmengKpiHelper;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiDuration;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiResult;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiUser;
import com.cmig.future.csportal.module.kpi.mapper.LjhKpiResultMapper;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiDurationService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiEventGroupService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiResultService;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiUserService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LjhKpiResultServiceImpl extends BaseServiceImpl<LjhKpiResult> implements ILjhKpiResultService{

	@Autowired
	private LjhKpiResultMapper ljhKpiResultMapper;

	@Autowired
	private ILjhKpiUserService ljhKpiUserService;

	@Autowired
	private ILjhKpiEventGroupService ljhKpiEventGroupService;

	@Autowired
	private ILjhKpiDurationService ljhKpiDurationService;

	@Override
	public List<LjhKpiResult>  select(LjhKpiResult dto, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return ljhKpiResultMapper.getReportForDate(dto);
	}

	@Override
	public List<LjhKpiResult> getReportForDate(LjhKpiResult dto) {
		return ljhKpiResultMapper.getReportForDate(dto);
	}

	@Override
	public void runResult(Date kpiDate)throws Exception {
		if(null==kpiDate){
			throw new RuntimeException("统计日期不能为空");
		}
		LjhKpiResult ljhKpiResult=new LjhKpiResult();
		//统计日期
		ljhKpiResult.setKpiDate(kpiDate);
		ljhKpiResultMapper.delete(ljhKpiResult);

		List<LjhKpiUser> userList=ljhKpiUserService.selectReportForDate(kpiDate,kpiDate);
		if(null!=userList&&userList.size()>0) {
			LjhKpiUser kpiUser = userList.get(0);
			//app名称
			ljhKpiResult.setAppName(kpiUser.getGroupName());
			//浏览量（PV）
			ljhKpiResult.setPageView(kpiUser.getLaunches());
			//访客数（UV）
			ljhKpiResult.setUniqueVisitor(kpiUser.getActiveUsers());
			//新访客数
			ljhKpiResult.setNewVisitor(kpiUser.getNewUsers());
			//老访客数
			ljhKpiResult.setOldVisitor(kpiUser.getActiveUsers()-kpiUser.getNewUsers());
			if(ljhKpiResult.getUniqueVisitor()>0) {
				//新访客比率
				ljhKpiResult.setNewVisitorPercent(Arith.div(ljhKpiResult.getNewVisitor(), ljhKpiResult.getUniqueVisitor(), 2));
				//老访客比率
				ljhKpiResult.setOldVisitorPercent(1 - ljhKpiResult.getNewVisitorPercent());
			}

			LjhKpiDuration ljhKpiDuration=new LjhKpiDuration();
			ljhKpiDuration.setKpiDate(kpiDate);
			ljhKpiDuration.setPeriodType(UmengKpiHelper.PERIOD_TYPE_DAILY);
			List<LjhKpiDuration> durationList=ljhKpiDurationService.selectReportForDate(ljhKpiDuration);
			if(null!=durationList&&durationList.size()>0){
				Long totalSeconds=0l;
				Long totalNum=0l;
				Double averageSeconds;
				for(LjhKpiDuration duration:durationList){
					String average=duration.getAverage();
					Long num=duration.getNum();
					try {
						Date startTime= DateUtils.parseDate("2018-02-01 00:00:00","YYYY-mm-dd HH:mm:ss");
						Date endTime= DateUtils.parseDate("2018-02-01 "+average,"YYYY-mm-dd HH:mm:ss");
						Long seconds= (endTime.getTime()-startTime.getTime())/1000;
						totalSeconds+=num*seconds;
						totalNum+=num;
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				averageSeconds= Arith.div(totalSeconds,totalNum,2);
				//平均访问时长（单位：秒）
				ljhKpiResult.setAccessSecondsAverage(averageSeconds);
			}
			super.mapper.insertSelective(ljhKpiResult);
		}
	}
}