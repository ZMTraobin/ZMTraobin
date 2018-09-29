package com.cmig.future.csportal.module.adv.service.impl;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.module.adv.dto.AppAdv;
import com.cmig.future.csportal.module.adv.dto.AppAdvReq;
import com.cmig.future.csportal.module.adv.dto.AppAdvSub;
import com.cmig.future.csportal.module.adv.dto.AppAdvVO;
import com.cmig.future.csportal.module.adv.mapper.AppAdvMapper;
import com.cmig.future.csportal.module.adv.service.IAppAdvService;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserMapper;
import com.cmig.future.csportal.module.zyyh.ZyyhConstants;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AppAdvServiceImpl extends BaseServiceImpl<AppAdv> implements IAppAdvService{
	
	String SORTDATA = "sortdata";
	String OFFLINE = "offline";
	String ONLINE = "online";

	@Autowired
	private AppAdvMapper appAdvMapper;
	
	@Autowired
    private AppUserMapper appUserMapper;
	
	/**
	 * APP端广告查询
	 */
	@Override
	public List<AppAdv> queryAppAdv(Long status) {
		AppAdv appAdv = new AppAdv();
		appAdv.setStatus(status);
		List<AppAdv> advs = appAdvMapper.queryAppAdv(appAdv);
		for(AppAdv adv : advs){
			String pic = Global.getFullImagePath(adv.getPic());
			adv.setPic(pic);
		}
		return advs;
	}
	
	/**
	 * 新版APP端广告查询
	 */
	@Override
	public JSONObject getNewAppAdv(String token){
		JSONObject advList = new JSONObject();
		JSONArray array = new JSONArray();
		//数据查询
		AppAdvReq appAdvReq = new AppAdvReq();
		appAdvReq.setStatus("4");
		List<AppAdvReq> appAdvReqs = appAdvMapper.queryNewAdvOfStatus(appAdvReq);
		
		AppUser appUser = null;
		if(!StringUtils.isEmpty(token)){
			String appUserId = UserTokenUtils.getUserIdByToken(token);
			if(!StringUtils.isEmpty(appUserId)){
				appUser = appUserMapper.selectByPrimaryKey(appUserId);
			}
        }
		for(AppAdvReq req : appAdvReqs){
			JSONObject adv = new JSONObject();
			//详细数据处理
			JSONArray detailList = new JSONArray();
			AppAdv appAdv = new AppAdv();
			appAdv.setGroupIdentifying(req.getGroupIdentifying());
			List<AppAdv> appAdvs = appAdvMapper.getEditAdv(appAdv);
			for(AppAdv detailAdv : appAdvs){
				JSONObject detail = new JSONObject();
				String pic = Global.getFullImagePath(detailAdv.getPic());
				detail.put("id", detailAdv.getAdvId());
				//永续贷URL动态替换
				String url = null == detailAdv.getUrl() ? "" : detailAdv.getUrl();
				Long isCas = detailAdv.getIsCas();//是否支持单点登录 1不传TGT 2传TGT不验证  3传TGT并验证
				String mobile = "";
				String openId = "";
				if(null != appUser && 3L == isCas){
					mobile = appUser.getMobile();
					openId = appUser.getSourceSystemId();
				}
				//http://fop.fitback.xin:8082/fop-web/yxd/index.do?channelNo=20171106105206977326&channelKey=42b5319c43fbf32cbf3937325d061f1e&mobileNo=18111111111&occType=123&openId=10101010
				if(!StringUtils.isEmpty(url) && url.contains(Global.getConfig("ZYYH.serverUrl"))){
					String yxdUrl = "%s/yxd/index.do?channelNo=%s&channelKey=%s&mobileNo=%s&occType=%s&openId=%s&url=%s";
					url = String.format(yxdUrl,Global.getConfig("ZYYH.serverUrl"),Global.getConfig("ZYYH.channelNo"),Global.getConfig("ZYYH.channelKey"),mobile,Global.getConfig("ZYYH.occType"),openId,Global.getConfig("ZYYH.yxd.index"));
				}
				detail.put("url", url);
				detail.put("pic", pic);
				detail.put("isCas", isCas);
				detail.put("title", detailAdv.getTitle());
				detail.put("advRank", detailAdv.getAdvRank());
				detail.put("urlType", detailAdv.getUrlType());
				detail.put("description", null == detailAdv.getDescription() ? "" : detailAdv.getDescription());
				detail.put("descriptionOne", null == detailAdv.getDescriptionOne() ? "" : detailAdv.getDescriptionOne());
				detail.put("descriptionTwo", null == detailAdv.getDescriptionTwo() ? "" : detailAdv.getDescriptionTwo());
				detailList.add(detail);
			}
			adv.put("advId", req.getGroupIdentifying());
			adv.put("title", req.getTitle());
			adv.put("status", req.getStatus());
			adv.put("advType", req.getAdvType());//
			adv.put("groupSort", req.getGroupSort());
			adv.put("desription", req.getDescription());
			adv.put("advDetailList", detailList);
			array.add(adv);
		}
		advList.put("advList", array);
		return advList;
	}
	
	/**
	 * 广告保存
	 */
	@Transactional
	public Boolean saveNewAdv(IRequest request, AppAdvVO appAdvs) {
		int size = appAdvs.getAppAdvs().size();
		Long userId = request.getUserId();
		List<AppAdv> appAdvList = appAdvs.getAppAdvs();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String title = appAdvs.getAdvTitle();
		Long type = appAdvs.getAdvType();
		for(AppAdv appAdv : appAdvList){
			appAdv.setTitle(title);
			appAdv.setAdvType(type);
			appAdv.setCreatedBy(userId);
			appAdv.setGroupIdentifying(uuid);
			appAdv.setStatus(1L);//
		}
		int num = appAdvMapper.saveAdvList(appAdvList);
		AppAdvSub appAdvSub = new AppAdvSub();
		appAdvSub.setGroupIdentifyingId(uuid);
		appAdvSub.setGroupSort(0L);
		int subnum = appAdvMapper.saveAdvSub(appAdvSub);
		return size+1 == num+subnum;
	}

	/**
	 * 分页查询
	 */
	@Override
	public List<AppAdvReq> queryNewAdv(IRequest request, AppAdvReq record, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<AppAdvReq> advs = appAdvMapper.queryNewAdv(record);
		for(AppAdvReq adv : advs){
			String pic = Global.getFullImagePath(adv.getPic());
			adv.setPic(pic);
		}
		return advs;
	}

	/**
	 * 查询广告编辑信息
	 */
	@Override
	public List<AppAdv> getNewOneAdv(IRequest request, AppAdv appAdv){
		List<AppAdv> advs = appAdvMapper.getEditAdv(appAdv);
		for(AppAdv adv : advs){
			String pic = Global.getFullImagePath(adv.getPic());
			adv.setPic(pic);
		}
		return advs;
	}

	/**
	 * 批量更新数据 true 成功 false 失败
	 */
	@Transactional
	public Boolean updateBatchAdv(IRequest request, AppAdvVO appAdvs) {
		List<AppAdv> list = appAdvs.getAppAdvs();
		if(0 == list.size()) return false;
		Long userId = request.getUserId();
		Long type = appAdvs.getAdvType();
		String title = appAdvs.getAdvTitle();
		Date date = new Date();
		for(AppAdv appAdv : list){
			appAdv.setTitle(title);
			appAdv.setAdvType(type);
			appAdv.setStatus(1L);//待审核
			appAdv.setLastUpdateDate(date);
			appAdv.setLastUpdatedBy(userId);
		}
		int result = appAdvMapper.updateBatchAdv(list);
		return list.size() == result;
	}
	
	/**
	 * 查询排序广告（已上线、已下线）
	 */
	@Override
	public List<Object> queryNewAdvSort(IRequest request) {
		List<Object> list = new ArrayList<>(1);
		AppAdvReq appAdvRO = new AppAdvReq();
		appAdvRO.setStatus("3");//待上线
		List<AppAdvReq> offline = appAdvMapper.queryNewAdvOfStatus(appAdvRO);
		appAdvRO.setStatus("4");//已上线
		List<AppAdvReq> online = appAdvMapper.queryNewAdvOfStatus(appAdvRO);
		Map<String,JSONArray> paramMap = new HashMap<>(2);
		//数据处理
		JSONArray arrayOff = new JSONArray();
		JSONArray arrayOn = new JSONArray();
		for(AppAdvReq off : offline){
			//1单图 2轮播 3左一右二 4单行三图
			String advType = off.getAdvType();
			JSONObject offJson = new JSONObject();
			offJson.put("groupId", off.getGroupIdentifying());
			String sort = off.getGroupSort();//单广告排序
			String title = off.getTitle();
			offJson.put("title", title);
			offJson.put("sort", sort);
			offJson.put("status", off.getStatus());
			offJson.put("type", advType);
			arrayOff.add(offJson);
		}
		for(AppAdvReq on : online){
			String advType = on.getAdvType();
			JSONObject onJson = new JSONObject();
			String sort = on.getGroupSort();
			onJson.put("groupId", on.getGroupIdentifying());
			String title = on.getTitle();
			onJson.put("title", title);
			onJson.put("sort", sort);
			onJson.put("status", on.getStatus());
			onJson.put("type", advType);
			arrayOn.add(onJson);
		}
		paramMap.put(OFFLINE, arrayOff);
		paramMap.put(ONLINE, arrayOn);
		list.add(paramMap);
		return list;
	}

	/**
	 * 保存/更新上下线排序数据
	 */
	@Transactional
	public Boolean updateNewAdvSort(IRequest request, List<Object> list) {
		List<AppAdvSub> appAdvSubs = new ArrayList<>();
		List<AppAdv> appAdvs = new ArrayList<>();
		for(Object objs : list){
			AppAdvSub appAdvSub = new AppAdvSub();
			AppAdv appAdv = new AppAdv();
			JSONObject obj = JSONObject.fromObject(objs);
			Long sort = Long.valueOf(obj.get("sort").toString());
			Long status = Long.valueOf(obj.get("status").toString());
			String groupId = obj.getString("groupId");
			appAdv.setGroupIdentifying(groupId);
			appAdv.setStatus(status);
			appAdvs.add(appAdv);//所有广告统一处理
			appAdvSub.setGroupIdentifyingId(groupId);
			appAdvSub.setGroupSort(sort);
			appAdvSubs.add(appAdvSub);//组排序单独处理
		}
		int advSize = appAdvMapper.updateBatchAdvByGroupId(appAdvs);
		int subSize = appAdvMapper.updateBatchAdvSub(appAdvSubs);
		return (advSize + subSize) >= list.size();
	}

	/**
	 * 根据groupIdentifying批量删除数据
	 */
	@Transactional
	public Boolean deleteBatchByGroupId(List<AppAdv> list) {
		StringBuilder groupIdentifyingSb = new StringBuilder();
		for(AppAdv appAdv : list){
			groupIdentifyingSb.append(appAdv.getGroupIdentifying()+",");
		}
		String groupIdentifying = groupIdentifyingSb.toString();
		groupIdentifying = groupIdentifying.substring(0, groupIdentifying.length()-1);
		int ok = appAdvMapper.deleteBatchByGroupId(groupIdentifying);
		return ok >= 1;
	}

	/**
	 * 根据groupIdentifying更新广告审核
	 */
	@Transactional
	public Boolean updateAdvExamine(AppAdv appAdv) {
		int ok = appAdvMapper.updateAdvByGroupIdentifying(appAdv);
		return ok >= 1;
	}

}