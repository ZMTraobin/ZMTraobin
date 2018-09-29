package com.cmig.future.csportal.module.adv.dto;

import javax.persistence.Table;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable=true)
@Table(name = "csp_app_adv_sub")
public class AppAdvSub{
	
	private String groupIdentifyingId;//分组辨识
	
	private Long groupSort;//分组排序

	public String getGroupIdentifyingId() {
		return groupIdentifyingId;
	}

	public void setGroupIdentifyingId(String groupIdentifyingId) {
		this.groupIdentifyingId = groupIdentifyingId;
	}

	public Long getGroupSort() {
		return groupSort;
	}

	public void setGroupSort(Long groupSort) {
		this.groupSort = groupSort;
	}

	@Override
	public String toString() {
		return "{\"AppAdvSub\":{\"groupIdentifying\":\"" + groupIdentifyingId + "\",\"groupSort\":\"" + groupSort
				+ "\"}}";
	}
	
}
