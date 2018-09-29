package com.cmig.future.csportal.module.sys.enumType;

/**
 * 消息分类 通知公告、抄表、报修、安防巡检、设备巡检、装修巡检
 * Created by zhangtao107@126.com on 2016/11/25.
 */
public enum NotifyType {
    NOTIFY, READ_METER, REPAIR, SECURITY, EQUIPMENT, DECORATION,OTHER;

    public static boolean contains(String type){
        for(NotifyType typeEnum : NotifyType.values()){
            if(typeEnum.name().equals(type)){
                return true;
            }
        }
        return false;
    }
}
