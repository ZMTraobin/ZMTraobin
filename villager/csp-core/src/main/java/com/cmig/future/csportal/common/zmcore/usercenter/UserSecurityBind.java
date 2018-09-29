package com.cmig.future.csportal.common.zmcore.usercenter;

/**
 * Created by zhangtao107@126.com on 2016/8/25.
 */
public class UserSecurityBind {

    private String mobileNo;// 绑定手机号 VARCHAR
    private String mobileNoUpdateTime;// 手机号绑定时间 VARCHAR
    private String email ;//绑定 Email VARCHAR
    private String emailUpdateTime ;//绑定时间 VARCHAR
    private String idCardType;// 绑定证件类型 VARCHAR
    private String idCardNo;// 绑定证件号码 VARCHAR
    private String idCardNoUpdateTime ;//证件绑定时间 VARCHAR


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileNoUpdateTime() {
        return mobileNoUpdateTime;
    }

    public void setMobileNoUpdateTime(String mobileNoUpdateTime) {
        this.mobileNoUpdateTime = mobileNoUpdateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailUpdateTime() {
        return emailUpdateTime;
    }

    public void setEmailUpdateTime(String emailUpdateTime) {
        this.emailUpdateTime = emailUpdateTime;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardNoUpdateTime() {
        return idCardNoUpdateTime;
    }

    public void setIdCardNoUpdateTime(String idCardNoUpdateTime) {
        this.idCardNoUpdateTime = idCardNoUpdateTime;
    }
}
