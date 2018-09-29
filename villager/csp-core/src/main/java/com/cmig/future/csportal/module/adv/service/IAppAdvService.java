package com.cmig.future.csportal.module.adv.service;

import java.util.List;

import com.cmig.future.csportal.module.adv.dto.AppAdv;
import com.cmig.future.csportal.module.adv.dto.AppAdvReq;
import com.cmig.future.csportal.module.adv.dto.AppAdvVO;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import net.sf.json.JSONObject;

public interface IAppAdvService extends IBaseService<AppAdv>, ProxySelf<IAppAdvService>{

	//APP端广告查询
	List<AppAdv> queryAppAdv(Long status);
	
	//新版APP端广告查询
	JSONObject getNewAppAdv(String token);
	
	//新修改接口
	//0.名称针对当前类型唯一性查询
//	Boolean onlyAdvTitle(String title);
	//1.保存
	Boolean saveNewAdv(IRequest request, AppAdvVO appAdvs);
	//2.分页查询
	List<AppAdvReq> queryNewAdv(IRequest request, AppAdvReq record, int pageNum, int pageSize);
	//3.查询广告编辑信息
	List<AppAdv> getNewOneAdv(IRequest request, AppAdv record);
	//4.批量更新数据
	Boolean updateBatchAdv(IRequest request, AppAdvVO appAdvs);
	//5.查询广告上下线数据
	List<Object> queryNewAdvSort(IRequest request);
	//6.保存/更新上下线排序数据
	Boolean updateNewAdvSort(IRequest request, List<Object> list);
	//7.根据groupIdentifying批量删除数据
	Boolean deleteBatchByGroupId(List<AppAdv> list);
	//8.根据groupIdentifying更新广告审核
	Boolean updateAdvExamine(AppAdv appAdv);
	
}