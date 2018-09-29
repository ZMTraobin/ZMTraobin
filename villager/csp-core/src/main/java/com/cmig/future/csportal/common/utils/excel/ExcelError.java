package com.cmig.future.csportal.common.utils.excel;

import java.io.Serializable;

/**
 * 此类描述的是：EXCEL数据导入 EXCEL数据校验错误信息模型类
 * 
 * @author:zhangtao107@126.com
 * @version: 2016-3-2 下午2:37:34
 */

public class ExcelError implements Serializable {

	public ExcelError() {
	}

	public ExcelError(String sheetName, int row, String title, String errorMessage) {
		this.sheetName = sheetName;
		this.row = row;
		this.title = title;
		this.errorMessage = errorMessage;
	}
	
	public ExcelError(String sheetName, int row, int column, String title, String errorMessage) {
		this.sheetName = sheetName;
		this.row = row;
		this.column = column;
		this.title = title;
		this.errorMessage = errorMessage;
	}

	/**
	* 工作表名
	*/
	private String sheetName;
	/**
	* 行号
	*/
	private int row;
	/**
	* 列号
	*/
	private int column;
	/**
	* 列名
	*/
	private String title;
	/**
	* 错误信息
	*/
	private String errorMessage;

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

}
