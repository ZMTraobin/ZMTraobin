package com.cmig.future.csportal.api.app.etl.controllers;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:42 2018/6/19.
 * @Modified by zhangtao on 14:42 2018/6/19.
 */
public class BaseResponse {
	private String code;
	private String msg;
	private Object data;
	private Long total;

	public BaseResponse() {
	}

	public BaseResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public BaseResponse(String code, String msg, Object data) {
		this(code,msg);
		this.data = data;
	}

	public BaseResponse(String code, String msg, Object data, Long total) {
		this(code,msg,data);
		this.total = total;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
