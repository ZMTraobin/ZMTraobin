package com.cmig.future.csportal.module.kpi.response;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:27 2018/1/25.
 * @Modified by zhangtao on 11:27 2018/1/25.
 */
public class UmengAppResponse {

	/**
	 * appkey : 576b59cde0f55a31940035f8
	 * category : 生活
	 * platform : iphone
	 * use_game_sdk : false
	 * created_at : 2016-06-23T03:38:53Z
	 * updated_at : 2017-03-08T09:47:49Z
	 * popular : 0
	 * name : 乐家慧(iOS)
	 */

	private String appkey;
	private String category;
	private String platform;
	private boolean use_game_sdk;
	private String created_at;
	private String updated_at;
	private int popular;
	private String name;

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public boolean getUse_game_sdk() {
		return use_game_sdk;
	}

	public void setUse_game_sdk(boolean use_game_sdk) {
		this.use_game_sdk = use_game_sdk;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public int getPopular() {
		return popular;
	}

	public void setPopular(int popular) {
		this.popular = popular;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
