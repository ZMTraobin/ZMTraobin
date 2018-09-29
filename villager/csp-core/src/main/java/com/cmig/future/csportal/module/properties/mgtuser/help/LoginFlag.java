package com.cmig.future.csportal.module.properties.mgtuser.help;

public enum LoginFlag {
    //默认启用,帐号状态 “启用”代表此账号允许登录，“停用”则表示此账号不允许登录

    enable("1","启用"),
    disable("0","停用");

    private String code;

    private String mean;

    LoginFlag(String code, String mean) {
        this.code = code;
        this.mean = mean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
