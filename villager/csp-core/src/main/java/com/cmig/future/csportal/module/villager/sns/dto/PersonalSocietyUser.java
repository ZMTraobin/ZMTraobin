package com.cmig.future.csportal.module.villager.sns.dto;


public class PersonalSocietyUser {
     //用户联系人id
	 private String societyUserId;
	 //联系人姓名
	 private String societyUserName;
	 //联系人头像
	 private String societyUserIcon;
	 //联系人昵称
	 private String societyNickName;
	 //是否登录过
	 private int everEntryStatus;
	 //村子的id
	 private String communityId;
	 //村子的id
	 private String communityName;
	 //村子的id
	 private boolean isAttention;
	 
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getSocietyUserId() {
		return societyUserId;
	}
	public void setSocietyUserId(String societyUserId) {
		this.societyUserId = societyUserId;
	}
	public String getSocietyUserName() {
		return societyUserName;
	}
	public void setSocietyUserName(String societyUserName) {
		this.societyUserName = societyUserName;
	}
	public String getSocietyUserIcon() {
		return societyUserIcon;
	}
	public void setSocietyUserIcon(String societyUserIcon) {
		this.societyUserIcon = societyUserIcon;
	}
	public String getSocietyNickName() {
		return societyNickName;
	}
	public void setSocietyNickName(String societyNickName) {
		this.societyNickName = societyNickName;
	}
	public int getEverEntryStatus() {
		return everEntryStatus;
	}
	public void setEverEntryStatus(int everEntryStatus) {
		this.everEntryStatus = everEntryStatus;
	}
	@Override
	public String toString() {
		return "PersonalSocietyUser [societyUserId=" + societyUserId + ", societyUserName=" + societyUserName
				+ ", societyUserIcon=" + societyUserIcon + ", societyNickName=" + societyNickName + ", everEntryStatus="
				+ everEntryStatus + ", communityId=" + communityId + ", communityName=" + communityName + "]";
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public boolean getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(boolean isAttention) {
		this.isAttention = isAttention;
	}
	

}
