package com.cmig.future.csportal.common.zmcore.usercenter;

/**
 * Created by zhangtao107@126.com on 2016/8/25.
 */
public class UserBasic {

    private String userId ;//用户 ID VARCHAR
    private String loginCode ;//用户登录名 VARCHAR
    private String userName;// 用户姓名 VARCHAR
    private String nickName ;//用户昵称 VARCHAR
    private String userType ;//用户类型 VARCHAR
    private String  userRegSource ;//用户注册来源 VARCHAR
    private String createTime ;//用户注册时间 VARCHAR
    private String custId ;//核心客户 ID VARCHAR


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserRegSource() {
        return userRegSource;
    }

    public void setUserRegSource(String userRegSource) {
        this.userRegSource = userRegSource;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
}
