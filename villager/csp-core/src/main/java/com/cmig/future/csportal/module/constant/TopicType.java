package com.cmig.future.csportal.module.constant;

public enum TopicType {

    video("video","视频"),
    text("text","文本"),
    image("image","图片"),
    ;

    private String code;
    private String name;

    TopicType(String code, String name) {
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
