package com.cmig.future.csportal.common.zmcore.usercenter;

/**
 * Created by zhangtao107@126.com on 2016/8/25.
 */
public class UserInfo {

    private String code ;
    private String message;

    private UserBasic userBasic;
    private UserSecurityBind userSecurityBind;
    private UserSales userSales;
    private UserReferrer userReferrer;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserBasic getUserBasic() {
        return userBasic;
    }

    public void setUserBasic(UserBasic userBasic) {
        this.userBasic = userBasic;
    }

    public UserSecurityBind getUserSecurityBind() {
        return userSecurityBind;
    }

    public void setUserSecurityBind(UserSecurityBind userSecurityBind) {
        this.userSecurityBind = userSecurityBind;
    }

    public UserSales getUserSales() {
        return userSales;
    }

    public void setUserSales(UserSales userSales) {
        this.userSales = userSales;
    }

    public UserReferrer getUserReferrer() {
        return userReferrer;
    }

    public void setUserReferrer(UserReferrer userReferrer) {
        this.userReferrer = userReferrer;
    }
}
