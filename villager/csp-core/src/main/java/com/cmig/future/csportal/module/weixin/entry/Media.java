package com.cmig.future.csportal.module.weixin.entry;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 15:54 2017/12/6.
 * @Modified by zhangtao on 15:54 2017/12/6.
 */
public class Media {


	/**
	 * errcode : 0
	 * errmsg :
	 * type : image
	 * media_id : 1G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0
	 * created_at : 1380000000
	 */

	private int errcode;
	private String errmsg;
	/**
	 * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
	 */
	private String type;
	private String media_id;
	private String created_at;

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
}
