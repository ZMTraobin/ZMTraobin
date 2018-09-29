package com.cmig.future.csportal.module.kpi.service.impl;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.kpi.UmengKpiHelper;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiUser;
import com.cmig.future.csportal.module.kpi.dto.UmengApp;
import com.cmig.future.csportal.module.kpi.response.UserBaseData;
import com.cmig.future.csportal.module.kpi.mapper.LjhKpiUserMapper;
import com.cmig.future.csportal.module.kpi.mapper.UmengAppMapper;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiUserService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LjhKpiUserServiceImpl extends BaseServiceImpl<LjhKpiUser> implements ILjhKpiUserService{

	@Autowired
	private LjhKpiUserMapper ljhKpiUserMapper;

	@Autowired
	private UmengAppMapper umengAppMapper;

	@Override
	public void synUserKpi(String date) throws Exception {
		List<UmengApp> apps= umengAppMapper.selectAll();
		if(null!=apps&&apps.size()>0){
			for(UmengApp app:apps) {
				UserBaseData userBaseData = UmengKpiHelper.getUserKpi(app.getAppKey(), date);
				if (null != userBaseData) {
					LjhKpiUser ljhKpiUser = new LjhKpiUser();
					ljhKpiUser.setAppKey(app.getAppKey());
					ljhKpiUser.setKpiDate(DateUtils.parseDate(date));
					super.mapper.delete(ljhKpiUser);
					ljhKpiUser.setNewUsers(userBaseData.getNew_users());
					ljhKpiUser.setActiveUsers(userBaseData.getActive_users());
					ljhKpiUser.setInstallations(userBaseData.getInstallations());
					ljhKpiUser.setLaunches(userBaseData.getLaunches());
					super.mapper.insertSelective(ljhKpiUser);
				}
				/**
				 * 友盟API接⼝限制⽤户每15分钟最多进⾏500次请求，
				 * 过度频率的调⽤API会导致你的IP暂时不能再次进⾏请求，15分钟后恢复。
				 */
				Thread.sleep(10000);
			}
		}
	}

	@Override
	public List<LjhKpiUser> selectReport(LjhKpiUser dto) {
		return ljhKpiUserMapper.selectReport(dto);
	}

	@Override
	public List<LjhKpiUser> selectReportForDate(Date startDate, Date endDate) {
		return ljhKpiUserMapper.selectReportForDate(startDate,endDate);
	}
}