package com.cmig.future.csportal.module.sys.notifyrecord.constants;

/**
 * Created by zhangtao107@126.com on 2017/3/30.
 */
public enum NotifyMessageTypeEnum {
    EMAIL("EMAIL"), SMS("SMS"), PUSH("PUSH");

    private String code;

    private NotifyMessageTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
