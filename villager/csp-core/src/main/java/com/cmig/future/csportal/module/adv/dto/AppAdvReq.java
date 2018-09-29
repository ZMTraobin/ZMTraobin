package com.cmig.future.csportal.module.adv.dto;

import java.io.Serializable;
import java.util.Date;

public class AppAdvReq implements Serializable{
	
	private static final long serialVersionUID = 3369097791175119999L;
	
	private Long advId;
	
	private String userName;
	
	private String title;
	
	private String advType;
	
	private String status;
	
	private String url;//*地址
	
	private String urlType;//
	
	private String pic;//
	
	private Date lastUpdateDate;
	
	private String isCas;
	
	private String description;
	
	private String advRank;//ADV_RANK排序大的在前
    
    private String groupIdentifying;//分组辨识
    
    private String groupSort = "0";//分组排序
    
    public String getAdvRank() {
		return advRank;
	}

	public void setAdvRank(String advRank) {
		this.advRank = advRank;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupSort() {
		return groupSort;
	}

	public void setGroupSort(String groupSort) {
		this.groupSort = groupSort;
	}

	public String getGroupIdentifying() {
		return groupIdentifying;
	}

	public void setGroupIdentifying(String groupIdentifying) {
		this.groupIdentifying = groupIdentifying;
	}
	
	public String getIsCas() {
		return isCas;
	}

	public void setIsCas(String isCas) {
		this.isCas = isCas;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Long getAdvId() {
		return advId;
	}

	public void setAdvId(Long advId) {
		this.advId = advId;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAdvType() {
		return advType;
	}

	public void setAdvType(String advType) {
		this.advType = advType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
