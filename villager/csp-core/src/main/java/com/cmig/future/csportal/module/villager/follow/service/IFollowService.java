package com.cmig.future.csportal.module.villager.follow.service;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.hand.hap.core.ProxySelf;

import java.util.List;
import java.util.Map;

public interface IFollowService extends ProxySelf<IFollowService>{

    /**
     * 获取粉丝数
     * @param userId
     * @return
     */
    int followedCount(String userId);
    /**
     * 获取关注数
     * @param userId
     * @return
     */
    int followerCount(String userId);

    Map addFollow(String followedId, String appUserId);

	Map queryFollowed(String uid, int page, int pageSize);

	Boolean queryFollowStatus(String followedId, String appUserId);

	Map queryFollower(String uid, int page, int pageSize);

    /**
     * 当前用户是否已关注目标用户
     * @param currentUserId
     * @param targetUserId
     * @return
     */
	boolean isAttention(String currentUserId,String targetUserId);
	
	Map delFollow(String followedId, String appUserId);
	
	List<String> findFolloweds(AppUser appUser);

}