package com.cmig.future.csportal.module.kpi.response;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:43 2018/1/25.
 * @Modified by zhangtao on 10:43 2018/1/25.
 */
public class UserBaseData {


	/**
	 * date : 2018-01-23
	 * active_users : 8
	 * installations : 7325
	 * launches : 27
	 * new_users : 2
	 */

	private String date;
	private Long active_users;
	private Long installations;
	private Long launches;
	private Long new_users;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getActive_users() {
		return active_users;
	}

	public void setActive_users(Long active_users) {
		this.active_users = active_users;
	}

	public Long getInstallations() {
		return installations;
	}

	public void setInstallations(Long installations) {
		this.installations = installations;
	}

	public Long getLaunches() {
		return launches;
	}

	public void setLaunches(Long launches) {
		this.launches = launches;
	}

	public Long getNew_users() {
		return new_users;
	}

	public void setNew_users(Long new_users) {
		this.new_users = new_users;
	}
}
