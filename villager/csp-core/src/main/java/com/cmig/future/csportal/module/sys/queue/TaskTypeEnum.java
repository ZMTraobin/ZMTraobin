package com.cmig.future.csportal.module.sys.queue;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:03 2017/12/20.
 * @Modified by zhangtao on 18:03 2017/12/20.
 */
public enum TaskTypeEnum {
	ORDER_CANCEL("ORDER_CANCEL","超时未支付订单取消任务");

	private String code;
	private String detail;

	TaskTypeEnum(String code, String detail) {
		this.code = code;
		this.detail = detail;
	}
}
