package com.cmig.future.csportal.module.base.enums;

/**
 * 消息分类 通知公告、抄表、报修、安防巡检、设备巡检、装修巡检
 * Created by zhangtao107@126.com on 2016/11/25.
 */
public enum NotifyCategoryEnum {
	NOTIFY("NOTIFY"), READ_METER("READ_METER"), REPAIR("REPAIR"), SECURITY("SECURITY"), EQUIPMENT("EQUIPMENT"), DECORATION("DECORATION"), CLEAN("CLEAN"),OTHER("OTHER");


	private String code;

	NotifyCategoryEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static boolean contains(String type){
		for(NotifyCategoryEnum typeEnum : NotifyCategoryEnum.values()){
			if(typeEnum.name().equals(type)){
				return true;
			}
		}
		return false;
	}
}
