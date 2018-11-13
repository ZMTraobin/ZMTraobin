package com.cmig.future.csportal.module.villager.voucher.dto;

public class VoucherStatus {
	//是否有效
	public static final String VALID_STATUS="0";
	public static final String INVALID_STATUS="1";
	//是否使用
	//领取成功未使用
	public static final String NOT_USED="1";
	//已经使用
	public static final String USED="0";
	//过期不能使用
	public static final String DISABLE_USED="2";
	//已点击使用，尚未处理
	public static final String IS_USEDING="3";
	//领取的频率
	public static final String ONCE_CYCLE="0";
	public static final String DAYS_CYCLE="1";
	public static final String LIMITLESS_CYCLE="2";
	

}
