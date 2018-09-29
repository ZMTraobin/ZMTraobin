package com.cmig.future.csportal.common.utils.excel;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:39 2017/7/14.
 * @Modified by zhangtao on 16:39 2017/7/14.
 */
public enum ExcelDataImportEnum {

	HOUSE("HOUSE","房屋信息"),
	OWNER("OWNER","业主信息"),
	STRUCTURE("STRUCTURE","建筑结构信息"),
	RECEIVABLE("RECEIVABLE","应收明细信息"),
	ORDER("ORDER","订单信息"),
	FAMILYINFO("FAMILYINFO","家庭信息");

	private String code;
	private String description;

	ExcelDataImportEnum(String code,String description){
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
