package com.cmig.future.csportal.module.weixin.entry;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:58 2017/11/28.
 * @Modified by zhangtao on 14:58 2017/11/28.
 */
public class User implements Serializable {

	/**
	 * errcode : 0
	 * errmsg : ok
	 * userid : zhangsan
	 * name : 李四
	 * department : [1,2]
	 * order : [1,2]
	 * position : 后台工程师
	 * mobile : 15913215421
	 * gender : 1
	 * email : zhangsan@gzdev.com
	 * isleader : 1
	 * avatar : http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0
	 * telephone : 020-123456
	 * english_name : jackzhang
	 * extattr : {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
	 * status : 1
	 */

	private int errcode;
	private String errmsg;
	private String userid;
	private String name;
	private String position;
	private String mobile;
	private String gender;
	private String email;
	private int isleader;
	private String avatar;
	private String telephone;
	private String english_name;
	private ExtattrBean extattr;
	private int status;
	private List<Integer> department;
	private List<Integer> order;

	private String workWxToken;

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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsleader() {
		return isleader;
	}

	public void setIsleader(int isleader) {
		this.isleader = isleader;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEnglish_name() {
		return english_name;
	}

	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}

	public ExtattrBean getExtattr() {
		return extattr;
	}

	public void setExtattr(ExtattrBean extattr) {
		this.extattr = extattr;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Integer> getDepartment() {
		return department;
	}

	public void setDepartment(List<Integer> department) {
		this.department = department;
	}

	public List<Integer> getOrder() {
		return order;
	}

	public void setOrder(List<Integer> order) {
		this.order = order;
	}

	public String getWorkWxToken() {
		return workWxToken;
	}

	public void setWorkWxToken(String workWxToken) {
		this.workWxToken = workWxToken;
	}

	public static class ExtattrBean {
		private List<AttrsBean> attrs;

		public List<AttrsBean> getAttrs() {
			return attrs;
		}

		public void setAttrs(List<AttrsBean> attrs) {
			this.attrs = attrs;
		}

		public static class AttrsBean {
			/**
			 * name : 爱好
			 * value : 旅游
			 */

			private String name;
			private String value;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getValue() {
				return value;
			}

			public void setValue(String value) {
				this.value = value;
			}
		}
	}
}
