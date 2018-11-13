package com.cmig.future.csportal.module.villager.follow.dto;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "xl_follow_relation")
public class FollowRelation {

      private String followedId;//被关注的ID

      private String followerId;//关注者的ID

      private Date followDate;

	public String getFollowedId() {
		return followedId;
	}

	public void setFollowedId(String followedId) {
		this.followedId = followedId;
	}

	public String getFollowerId() {
		return followerId;
	}

	public void setFollowerId(String followerId) {
		this.followerId = followerId;
	}

	public Date getFollowDate() {
		return followDate;
	}

	public void setFollowDate(Date followDate) {
		this.followDate = followDate;
	}

     }
