package com.cmig.future.csportal.module.weixin;

import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 18:27 2017/12/5.
 * @Modified by zhangtao on 18:27 2017/12/5.
 */
public class WorkWxException extends RuntimeException {
	private String errcode;

	public WorkWxException(String errcode) {
		super();
		this.errcode=errcode;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	@Override
	public String getMessage() {
		return WorkWxHelper.getErrorMsg(this.errcode);
	}
}
