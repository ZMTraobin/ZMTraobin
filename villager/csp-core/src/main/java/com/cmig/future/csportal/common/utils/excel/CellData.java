package com.cmig.future.csportal.common.utils.excel;

import java.io.Serializable;

public class CellData implements Serializable{

	public CellData() {
	}
	
	public CellData(String sheetName,int row,int column,String title,String name,String value) {
		this.sheetName=sheetName;
		this.row = row;
		this.column = column;
		this.title = title;
		this.name = name;
		this.value = value;
	}	
	//sheet名
	private String sheetName;
	// 行
	private int row;
	// 列
	private int column;
	// 属性中文描述
	private String title;
	// 属性名
	private String name;
	
	// 属性值
	private String value;

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	

}
