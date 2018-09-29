package com.cmig.future.csportal.module.weixin.helper;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:46 2017/12/12.
 * @Modified by zhangtao on 16:46 2017/12/12.
 */
public enum MessageType {

	/**
	 * mpnews类型的图文消息，跟普通的图文消息一致，唯一的差异是图文内容存储在企业微信。
	 * 多次发送mpnews，会被认为是不同的图文，阅读、点赞的统计会被分开计算。
	 */

	text("text", "文本消息"),
	image("image", "图片消息"),
	voice("voice", "语音消息"),
	video("video", "视频消息"),
	file("file", "文件消息"),
	textcard("textcard", "文本卡片消息"),
	news("news", "图文消息"),
	mpnews("mpnews", "企业图文消息"),
	event("event", "事件消息"),;


	private String code;
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

	MessageType(String code, String detail) {
		this.code = code;
		this.detail = detail;
	}

	public static boolean contains(String code) {
		MessageType[] season = values();
		for (MessageType s : season) {
			if (s.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
}
