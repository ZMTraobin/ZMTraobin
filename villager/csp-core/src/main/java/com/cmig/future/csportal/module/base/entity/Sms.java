package com.cmig.future.csportal.module.base.entity;

import java.io.Serializable;

/*
 * zsj
 */
public class Sms implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mobile; //手机号
	private String code;  //验证吗
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
