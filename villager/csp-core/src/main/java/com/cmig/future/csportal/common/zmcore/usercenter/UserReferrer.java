package com.cmig.future.csportal.common.zmcore.usercenter;

/**
 * Created by zhangtao107@126.com on 2016/8/25.
 */
public class UserReferrer {

    private String referrerId;// 推荐人 uid VARCHAR
    private String createTime;// 绑定时间 VARCHAR

    public String getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(String referrerId) {
        this.referrerId = referrerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
