package com.cmig.future.csportal.common.zmcore.usercenter;

/**
 * Created by zhangtao107@126.com on 2016/8/25.
 */
public class UserSales {

    private String salesNo;// 财富经理员工号 VARCHAR 注：财富经理姓名、手机号码等详细需要调用核心 SALES0004 接口
    private String  createTime;// 绑定时间 VARCHAR

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
