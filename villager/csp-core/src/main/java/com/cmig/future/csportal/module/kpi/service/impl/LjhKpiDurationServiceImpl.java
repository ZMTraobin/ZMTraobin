package com.cmig.future.csportal.module.kpi.service.impl;

import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.kpi.UmengKpiHelper;
import com.cmig.future.csportal.module.kpi.response.Duration;
import com.cmig.future.csportal.module.kpi.dto.LjhKpiDuration;
import com.cmig.future.csportal.module.kpi.dto.UmengApp;
import com.cmig.future.csportal.module.kpi.mapper.LjhKpiDurationMapper;
import com.cmig.future.csportal.module.kpi.mapper.UmengAppMapper;
import com.cmig.future.csportal.module.kpi.service.ILjhKpiDurationService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LjhKpiDurationServiceImpl extends BaseServiceImpl<LjhKpiDuration> implements ILjhKpiDurationService{

	@Autowired
	private LjhKpiDurationMapper ljhKpiDurationMapper;

	@Autowired
	private UmengAppMapper umengAppMapper;

	@Override
	public void synDurationKpi(String date, String periodType) throws Exception {
		List<UmengApp> apps= umengAppMapper.selectAll();
		if(null!=apps&&apps.size()>0){
			for(UmengApp app:apps) {
				Duration duration= UmengKpiHelper.getDurationKpi(app.getAppKey(),date,periodType);
				if(null!=duration){
					List<Duration.DataBean> list=duration.getData();
					if(null!=list&&list.size()>0){
						LjhKpiDuration ljhKpiDuration=new LjhKpiDuration();
						ljhKpiDuration.setAppKey(app.getAppKey());
						ljhKpiDuration.setPeriodType(periodType);
						ljhKpiDuration.setKpiDate(DateUtils.parseDate(date));
						super.mapper.delete(ljhKpiDuration);
						for(Duration.DataBean dataBean:list) {
							ljhKpiDuration.setDimension(dataBean.getKey());
							ljhKpiDuration.setNum(dataBean.getNum());
							ljhKpiDuration.setPercent(dataBean.getPercent());
							ljhKpiDuration.setAverage(duration.getAverage());
							super.mapper.insertSelective(ljhKpiDuration);
						}
					}
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
	public List<LjhKpiDuration> selectReportForDate(LjhKpiDuration ljhKpiDuration) {
		return ljhKpiDurationMapper.selectReportForDate(ljhKpiDuration);
	}
}