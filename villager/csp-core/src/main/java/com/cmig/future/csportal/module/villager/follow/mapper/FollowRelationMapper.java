package com.cmig.future.csportal.module.villager.follow.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.villager.follow.dto.FollowRelation;

public interface FollowRelationMapper extends Mapper<FollowRelation>{
	
	List<String> findFolloweds(AppUser appUser);

}