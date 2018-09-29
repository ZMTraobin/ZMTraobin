package com.cmig.future.csportal.module.weixin.helper;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 13:50 2017/12/21.
 * @Modified by zhangtao on 13:50 2017/12/21.
 */
public enum ChangeType {

	create_user("create_user","新增成员事件"),
	update_user("update_user","更新成员事件"),
	delete_user("delete_user","删除成员事件"),
	create_party("create_party","新增部门事件"),
	update_party("update_party","更新部门事件"),
	delete_party("delete_party","删除部门事件"),
	update_tag("update_tag","标签成员变更事件"),;

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

	ChangeType(String code, String detail) {
		this.code = code;
		this.detail = detail;
	}

	public static boolean contains(String code) {
		ChangeType[] season = values();
		for (ChangeType s : season) {
			if (s.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}
}
