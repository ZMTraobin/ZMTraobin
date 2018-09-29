package com.cmig.future.csportal.module.kpi.service.impl;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.kpi.UmengKpiHelper;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiEventGroup;
import com.cmig.future.csportal.module.kpi.dto.UmengApp;
import com.cmig.future.csportal.module.kpi.mapper.LjhKpiEventGroupMapper;
import com.cmig.future.csportal.module.kpi.mapper.UmengAppMapper;
import com.cmig.future.csportal.module.kpi.response.UmengEventGroup;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiEventGroupService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LjhKpiEventGroupServiceImpl extends BaseServiceImpl<LjhKpiEventGroup> implements ILjhKpiEventGroupService{

	@Autowired
	private UmengAppMapper umengAppMapper;

	@Autowired
	private LjhKpiEventGroupMapper ljhKpiEventGroupMapper;

	@Override
	public void synEventGroupKpi(String date) throws Exception {
		List<UmengApp> apps= umengAppMapper.selectAll();
		if(null!=apps&&apps.size()>0){
			for(UmengApp app:apps) {
				int pageNo=1,pageSize=50;
				List<UmengEventGroup> list;
				LjhKpiEventGroup ljhKpiEventGroup = new LjhKpiEventGroup();
				ljhKpiEventGroup.setAppKey(app.getAppKey());
				ljhKpiEventGroup.setKpiDate(DateUtils.parseDate(date));
				super.mapper.delete(ljhKpiEventGroup);
				do{
					list = UmengKpiHelper.getEventGroupKpi(app.getAppKey(), date,pageSize,pageNo);
					if (null!=list&&list.size()>0) {
						for(UmengEventGroup umengEventGroup:list) {
							ljhKpiEventGroup = new LjhKpiEventGroup();
							ljhKpiEventGroup.setAppKey(app.getAppKey());
							ljhKpiEventGroup.setKpiDate(DateUtils.parseDate(date));
							ljhKpiEventGroup.setGroupId(umengEventGroup.getGroup_id());
							ljhKpiEventGroup.setName(umengEventGroup.getName());
							ljhKpiEventGroup.setDisplayName(umengEventGroup.getDisplay_name());
							ljhKpiEventGroup.setNum(umengEventGroup.getCount());
							super.mapper.insertSelective(ljhKpiEventGroup);
						}
					}
					pageNo++;
					/**
					 * 友盟API接⼝限制⽤户每15分钟最多进⾏500次请求，
					 * 过度频率的调⽤API会导致你的IP暂时不能再次进⾏请求，15分钟后恢复。
					 */
					Thread.sleep(10000);
				}while (null!=list&&list.size()==pageSize);
			}
		}
	}

	@Override
	public List<LjhKpiEventGroup> selectReport(int days, int topNum) {
		/*
		List<LjhKpiEventGroup> dateList=ljhKpiEventGroupMapper.getHaveDataDates(days);
		if(null!=dateList&&dateList.size()>0){
			Date endDate=dateList.get(0).getKpiDate();
			Date startDate=dateList.get(dateList.size()-1).getKpiDate();
			return ljhKpiEventGroupMapper.selectReport(startDate,endDate,topNum);
		}
		return null;
		*/
		Date endDate=DateUtils.addDays(DateUtils.parseDate(DateUtils.getDate()),-1);
		Date startDate=DateUtils.addDays(endDate,-days);
		return ljhKpiEventGroupMapper.selectReport(startDate,endDate,topNum);
	}

	@Override
	public List<LjhKpiEventGroup> selectReportForDate(Date startDate, Date endDate) {
		return ljhKpiEventGroupMapper.selectReportForDate(startDate,endDate);
	}
}