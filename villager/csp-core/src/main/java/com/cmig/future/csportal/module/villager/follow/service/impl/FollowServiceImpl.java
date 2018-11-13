package com.cmig.future.csportal.module.villager.follow.service.impl;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.mapper.AppUserMapper;
import com.cmig.future.csportal.module.villager.follow.dto.FollowRelation;
import com.cmig.future.csportal.module.villager.follow.mapper.FollowRelationMapper;
import com.cmig.future.csportal.module.villager.follow.service.IFollowService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.mybatis.util.StringUtil;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class FollowServiceImpl implements IFollowService{

	@Autowired
	FollowRelationMapper followRelationMapper;
	
	@Autowired
	AppUserMapper appUserMapper;
	
    @Override
    public int followedCount(String userId) {
        //粉丝数
        FollowRelation selrel = new FollowRelation();
        selrel.setFollowedId(userId);
        int followedCount = followRelationMapper.selectCount(selrel);
        return followedCount;
    }

    @Override
    public int followerCount(String userId) {
        //关注数
        FollowRelation selrel = new FollowRelation();
        selrel.setFollowerId(userId);
        int followerCount = followRelationMapper.selectCount(selrel);
        return followerCount;
    }
    
    @Override
	public Map addFollow(String followedId, String appUserId) {
		// 同时关注多人
		String[] ids = followedId.split(",");
		for(String id : ids) {
			if(!StringUtil.isEmpty(id)) {
				// 添加关注
				FollowRelation addrel = new FollowRelation();
				addrel.setFollowedId(id);
				addrel.setFollowerId(appUserId);
				// 查询是否已关注
				int count = followRelationMapper.selectCount(addrel);
				if(count == 0) {// 未关注则执行插入
					followRelationMapper.insertSelective(addrel);
				}
			}
		}
		
		// 设置返回值
		FollowRelation selrel = new FollowRelation();
		selrel.setFollowedId(ids[0]);
		int followCount = followRelationMapper.selectCount(selrel);
		boolean followStatus = true;
		Map map = new HashedMap();
		map.put("followCount", followCount);
        map.put("followStatus", followStatus);
		return map;
	}

	@Override
	public Map queryFollowed(String uid, int page, int pageSize) {
		
		Map map = new HashedMap();
		// 查询关注列表--我关注的
		FollowRelation selrel = new FollowRelation();
		selrel.setFollowerId(uid);
		List<FollowRelation> flist = followRelationMapper.select(selrel);
		if(flist == null || flist.size() == 0) {
			// 设置返回值
			map.put("followCount", 0);//关注数
	        map.put("followList", null);//关注列表
			return map;
		}
		String[] ids = new String[flist.size()];
		for(int i=0;i<flist.size();i++) {
			ids[i]=flist.get(i).getFollowedId();
		}
		// 查询用户
		PageHelper.startPage(page,pageSize);
		List<Map> ulist = appUserMapper.queryFollowUser(ids);
		// 设置返回值
		map.put("followCount", flist.size());//关注数
        map.put("followList", ulist);//关注列表
		return map;
	}

	@Override
	public Boolean queryFollowStatus(String followedId, String appUserId) {
		Boolean status = false;
		FollowRelation selrel = new FollowRelation();
		selrel.setFollowerId(followedId);
		selrel.setFollowerId(appUserId);
		int cnt = followRelationMapper.selectCount(selrel);
		if(cnt > 0) {
			status = true;
		}
		return status;
	}

	@Override
	public Map queryFollower(String uid, int page, int pageSize) {
		Map map = new HashedMap();
		// 查询关注列表--关注我的
		FollowRelation selrel = new FollowRelation();
		selrel.setFollowedId(uid);
		List<FollowRelation> flist = followRelationMapper.select(selrel);
		if(flist == null || flist.size() == 0) {
			// 设置返回值
			map.put("followCount", 0);//粉丝数
	        map.put("followList",null);//粉丝列表
			return map;
		}
		String[] ids = new String[flist.size()];
		for(int i=0;i<flist.size();i++) {
			ids[i]=flist.get(i).getFollowerId();
		}
		// 查询用户
		PageHelper.startPage(page,pageSize);
		List<Map> ulist = appUserMapper.queryFollowUser(ids);
		
		// 设置返回值
		map.put("followCount", flist.size());//粉丝数
        map.put("followList", ulist);//粉丝列表
		return map;
	}

    @Override
    public boolean isAttention(String currentUserId, String targetUserId) {
        FollowRelation selrel = new FollowRelation();
        selrel.setFollowedId(targetUserId);
        selrel.setFollowerId(currentUserId);
        List<FollowRelation> flist = followRelationMapper.select(selrel);
        if(flist!=null&&flist.size()>0){
            return true;
        }
        return false;
    }

	@Override
	public Map delFollow(String followedId, String appUserId) {
		Map map = new HashedMap();
		// 取消关注
		FollowRelation addrel = new FollowRelation();
		addrel.setFollowedId(followedId);
		addrel.setFollowerId(appUserId);
		followRelationMapper.delete(addrel);
		// 设置返回值
		FollowRelation selrel = new FollowRelation();
		selrel.setFollowedId(followedId);
		int followCount = followRelationMapper.selectCount(selrel);
		boolean followStatus = false;
		map.put("followCount", followCount);
        map.put("followStatus", followStatus);
		return map;
	}

	@Override
	public List<String> findFolloweds(AppUser appUser) {
		// TODO Auto-generated method stub
		return followRelationMapper.findFolloweds(appUser);
	}

}