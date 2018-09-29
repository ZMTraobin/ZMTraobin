package com.cmig.future.csportal.module.kpi.service.impl;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.kpi.UmengKpiHelper;
import com.cmig.future.csportal.module.kpi.dto.UmengApp;
import com.cmig.future.csportal.module.kpi.response.UmengAppResponse;
import com.cmig.future.csportal.module.kpi.mapper.UmengAppMapper;
import com.cmig.future.csportal.module.kpi.service.IUmengAppService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UmengAppServiceImpl extends BaseServiceImpl<UmengApp> implements IUmengAppService{

	@Autowired
	private UmengAppMapper umengAppMapper;

	@Override
	public void synApps() throws Exception {
		List<UmengAppResponse> list=UmengKpiHelper.getApps();
		if(null!=list&&list.size()>0){
			for(UmengAppResponse entry:list){
				UmengApp umengApp=new UmengApp();
				umengApp.setAppKey(entry.getAppkey());
				List<UmengApp> appList=umengAppMapper.select(umengApp);
				if(null!=appList&&appList.size()>0){
					umengApp=appList.get(0);
				}
				umengApp.setPlatform(entry.getPlatform());
				umengApp.setName(entry.getName());
				umengApp.setPopular(new Integer(entry.getPopular()).toString());
				umengApp.setUseGameSdk(new Boolean(entry.getUse_game_sdk()).toString());
				if(!StringUtils.isEmpty(umengApp.getId())){
					super.mapper.updateByPrimaryKeySelective(umengApp);
				}else{
					super.mapper.insertSelective(umengApp);
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