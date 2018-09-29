package com.cmig.future.csportal.module.adv.dto;

import java.io.Serializable;
import java.util.List;

/**
 * class name: AppAdvs 
 * author: fri
 * date: 2017年8月18日
 * function: 多广告接收
 */
public class AppAdvVO implements Serializable{

	private static final long serialVersionUID = -3520622017320522185L;

	private List<AppAdv> appAdvs;//广告集合
	
	private String advTitle;//广告名称
	
	private Long advType;//广告位置 1单图 2轮播 3左一右二 4单行三图

	public List<AppAdv> getAppAdvs() {
		return appAdvs;
	}

	public void setAppAdvs(List<AppAdv> appAdvs) {
		this.appAdvs = appAdvs;
	}

	public String getAdvTitle() {
		return advTitle;
	}

	public void setAdvTitle(String advTitle) {
		this.advTitle = advTitle;
	}

	public Long getAdvType() {
		return advType;
	}
	
	

	public void setAdvType(Long advType) {
		this.advType = advType;
	}

	@Override
	public String toString() {
		return "{\"AppAdvs\":{\"appAdvs\":\"" + appAdvs.toString() + "\",\"advTitle\":\"" + advTitle + "\",\"advType\":\""
				+ advType + "\"}}";
	}
	
}
