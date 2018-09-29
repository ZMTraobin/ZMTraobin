package com.cmig.future.csportal.module.sys.appconfig.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.sys.appconfig.dto.AppCommunityFunction;
import com.cmig.future.csportal.module.sys.appconfig.mapper.AppCommunityFunctionMapper;
import com.cmig.future.csportal.module.sys.appconfig.mapper.AppConfigFunctionMapper;
import com.cmig.future.csportal.module.sys.appconfig.service.IAppCommunityFunctionService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppCommunityFunctionServiceImpl extends BaseServiceImpl<AppCommunityFunction> implements IAppCommunityFunctionService {

	@Autowired
	private AppCommunityFunctionMapper appCommunityFunctionMapper;

	@Autowired
	private AppConfigFunctionMapper appConfigFunctionMapper;

	@Override
	public void batchAdd(String[] functionIds, String[] appConfigCommunityIds, String[] orderNums) {
		if(null!=functionIds&&null!= appConfigCommunityIds&&null!= orderNums){
			for(String appConfigCommunityId: appConfigCommunityIds){
				if(StringUtils.isEmpty(appConfigCommunityId)) continue ;
				//已存在的功能信息查询
				AppCommunityFunction param=new AppCommunityFunction();
				param.setCid(appConfigCommunityId);
				List<AppCommunityFunction> existsList=appCommunityFunctionMapper.select(param);
				long maxNum=0;
				for(int i=0;i<functionIds.length;i++){
					if(StringUtils.isEmpty(functionIds[i])) continue ;
					AppCommunityFunction appCommunityFunction = new AppCommunityFunction();
					appCommunityFunction.setId(IdGen.uuid());
					appCommunityFunction.setCid(appConfigCommunityId);
					appCommunityFunction.setFid(functionIds[i]);
					appCommunityFunction.setNumber(Long.parseLong(orderNums[i]));
					appCommunityFunctionMapper.insertSelective(appCommunityFunction);

					int index;
					if((index=existsList.indexOf(appCommunityFunction))>=0){
						appCommunityFunctionMapper.deleteByPrimaryKey(existsList.get(index));
						existsList.remove(index);
					}
					if(i==functionIds.length-1){
						maxNum=Long.parseLong(orderNums[i]);
					}
				}
				//已存在的功能依次排在批量配置的功能后面
				if(maxNum>0) {
					for (AppCommunityFunction existsObj : existsList) {
						existsObj.setNumber(++maxNum);
						appCommunityFunctionMapper.updateByPrimaryKeySelective(existsObj);
					}
				}
			}
		}
	}

	@Override
	public void batchDeleteByCid(String[] appConfigCommunityIds) {
		if(null!= appConfigCommunityIds){
			appCommunityFunctionMapper.batchDeleteByCid(appConfigCommunityIds);
		}
	}

	@Override
	public void updateCommuntiyConfig(String[] functionIds, String appConfigCommunityId, String[] orderNums) {
		if(!StringUtils.isEmpty(appConfigCommunityId)){
			appCommunityFunctionMapper.batchDeleteByCid(new String[]{appConfigCommunityId});
			for(int i=0;i<functionIds.length;i++) {
				if (StringUtils.isEmpty(functionIds[i])) continue;
				AppCommunityFunction appCommunityFunction = new AppCommunityFunction();
				appCommunityFunction.setId(IdGen.uuid());
				appCommunityFunction.setCid(appConfigCommunityId);
				appCommunityFunction.setFid(functionIds[i]);
				appCommunityFunction.setNumber(Long.parseLong(orderNums[i]));
				appCommunityFunctionMapper.insertSelective(appCommunityFunction);
			}
		}
	}
}