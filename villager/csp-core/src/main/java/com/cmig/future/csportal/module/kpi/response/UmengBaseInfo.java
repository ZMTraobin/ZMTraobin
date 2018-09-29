package com.cmig.future.csportal.module.kpi.response;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:38 2018/1/25.
 * @Modified by zhangtao on 16:38 2018/1/25.
 */
public class UmengBaseInfo {

	/**
	 * today_active_users : 16
	 * yesterday_launches : 29
	 * installations : 11116
	 * today_launches : 54
	 * yesterday_active_users : 10
	 * yesterday_new_users : 2
	 * today_new_users : 3
	 */

	private String title;
	private int today_active_users;
	private int yesterday_launches;
	private int installations;
	private int today_launches;
	private int yesterday_active_users;
	private int yesterday_new_users;
	private int today_new_users;

	public int getToday_active_users() {
		return today_active_users;
	}

	public void setToday_active_users(int today_active_users) {
		this.today_active_users = today_active_users;
	}

	public int getYesterday_launches() {
		return yesterday_launches;
	}

	public void setYesterday_launches(int yesterday_launches) {
		this.yesterday_launches = yesterday_launches;
	}

	public int getInstallations() {
		return installations;
	}

	public void setInstallations(int installations) {
		this.installations = installations;
	}

	public int getToday_launches() {
		return today_launches;
	}

	public void setToday_launches(int today_launches) {
		this.today_launches = today_launches;
	}

	public int getYesterday_active_users() {
		return yesterday_active_users;
	}

	public void setYesterday_active_users(int yesterday_active_users) {
		this.yesterday_active_users = yesterday_active_users;
	}

	public int getYesterday_new_users() {
		return yesterday_new_users;
	}

	public void setYesterday_new_users(int yesterday_new_users) {
		this.yesterday_new_users = yesterday_new_users;
	}

	public int getToday_new_users() {
		return today_new_users;
	}

	public void setToday_new_users(int today_new_users) {
		this.today_new_users = today_new_users;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
