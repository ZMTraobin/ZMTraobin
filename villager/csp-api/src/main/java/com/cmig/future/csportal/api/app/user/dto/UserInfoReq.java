package com.cmig.future.csportal.api.app.user.dto;

public class UserInfoReq {
	
	private String tgt;
	
	private String page;
	
	private String size;
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTgt() {
		return tgt;
	}

	public void setTgt(String tgt) {
		this.tgt = tgt;
	}

}
