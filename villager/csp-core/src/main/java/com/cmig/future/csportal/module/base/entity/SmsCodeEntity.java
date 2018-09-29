package com.cmig.future.csportal.module.base.entity;


import java.io.Serializable;
import java.util.Date;

public class SmsCodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	/**
	* 手机号
	*/
	private String mobile;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	* 短信验证码
	*/
	private String smsCode ;

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
