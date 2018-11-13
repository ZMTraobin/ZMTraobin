package com.cmig.future.csportal.module.sys.logs.help;

public enum LoginType {
    APP_USER("APP_USER","C端用户登录"),
    MGT_USER("MGT_USER","B端用户登录");

    private String code;
    private String name;

    LoginType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
