package com.cmig.future.csportal.common.oauth2.entity;

public class ResponseEntity {
	
	private String status;	// 状态
	private String message;	// 消息
	private Object data;	// 数据
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
