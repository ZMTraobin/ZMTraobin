package com.cmig.future.csportal.module.adv.mapper;

import java.util.List;

import com.cmig.future.csportal.module.adv.dto.AppAdv;
import com.cmig.future.csportal.module.adv.dto.AppAdvReq;
import com.cmig.future.csportal.module.adv.dto.AppAdvSub;
import com.hand.hap.mybatis.common.Mapper;

public interface AppAdvMapper extends Mapper<AppAdv>{
	
	List<AppAdv> queryAppAdv(AppAdv appAdv);
	
	List<AppAdv> selecteAdv();

	//0.广告名称唯一性校验
//	String onlyAdvTitle(String title);
	//1.保存
	int saveAdvList(List<AppAdv> appAdvs);
	//2.查询
	List<AppAdvReq> queryNewAdv(AppAdvReq record);
	//3.单组广告或单条查询
	List<AppAdv> getEditAdv(AppAdv appAdv);
	//4.批量更新
	int updateBatchAdv(List<AppAdv> appAdvs);
	//5.向sub广告子表添加数据
	int saveAdvSub(AppAdvSub appAdvSub);
	//6.更新sub广告子表数据
	int updateAdvSub(AppAdvSub appAdvSub);
	//7.查询广告相关表中顺序
	List<AppAdvReq> queryNewAdvOfStatus(AppAdvReq record);
	//8.批量更新广告排序子表
	int updateBatchAdvSub(List<AppAdvSub> appAdvSubs);
	//9.根据组ID批量更新广告数据
	int updateBatchAdvByGroupId(List<AppAdv> appAdvs);
	//10.根据groupIdentifying更新数据
	int updateAdvByGroupIdentifying(AppAdv appAdv);
	//11.根据groupIdentifying批量删除数据
	int deleteBatchByGroupId(String groupIdentifying);
}