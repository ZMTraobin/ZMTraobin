package com.cmig.future.csportal.module.weixin.helper;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:46 2017/12/12.
 * @Modified by zhangtao on 16:46 2017/12/12.
 */
public enum EventType {

	subscribe("subscribe","订阅"),
	unsubscribe("unsubscribe","取消订阅"),
	enter_agent("enter_agent","进入应用"),
	location("location","上报地理位置"),
	batch_job_result("batch_job_result","异步任务完成事件推送"),
	change_contact("change_contact","通讯录变更事件"),
	click("click","点击菜单拉取消息的事件推送"),
	view("view","点击菜单跳转链接的事件推送"),
	scancode_push("scancode_push","扫码推事件的事件推送"),
	scancode_waitmsg("scancode_waitmsg","扫码推事件且弹出“消息接收中”提示框的事件推送"),
	pic_sysphoto("pic_sysphoto","弹出系统拍照发图的事件推送"),
	pic_photo_or_album("pic_photo_or_album","弹出拍照或者相册发图的事件推送"),
	pic_weixin("pic_weixin","弹出微信相册发图器的事件推送"),
	location_select("location_select","弹出地理位置选择器的事件推送"),;


	private String code ;
	private String detail;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	EventType(String code, String detail) {
		this.code = code;
		this.detail = detail;
	}

	public static boolean contains(String code) {
		EventType[] season = values();
		for (EventType s : season) {
			if (s.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
}
