package com.cmig.future.csportal.module.kpi.response;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:16 2018/2/2.
 * @Modified by zhangtao on 10:16 2018/2/2.
 */
public class UmengEventGroup {

	/**
	 * count : 5
	 * name : event_click_my_tab
	 * display_name : 我的tab
	 * group_id : 57a989e5e0f55a1a1e002ad7
	 */

	private long count;
	private String name;
	private String display_name;
	private String group_id;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
}
