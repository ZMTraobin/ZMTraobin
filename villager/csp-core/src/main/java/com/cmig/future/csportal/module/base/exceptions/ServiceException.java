package com.cmig.future.csportal.module.base.exceptions;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.enums.ExceptionEnum;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 9:49 2017/7/5.
 * @Modified by zhangtao on 9:49 2017/7/5.
 */
public class ServiceException extends RuntimeException {

	private Integer code;

	public String extMsg;

	public ServiceException(ExceptionEnum e){
		super(e.getMessage());
		this.code=e.getCode();
	}

	public ServiceException(ExceptionEnum e, String extMsg){
		super(e.getMessage());
		this.code=e.getCode();
		this.extMsg=extMsg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		if(!StringUtils.isEmpty(this.extMsg)) {
			return super.getMessage()+","+extMsg;
		}else{
			return super.getMessage();
		}
	}

	public String getExtMsg() {
		return extMsg;
	}

	public void setExtMsg(String extMsg) {
		this.extMsg = extMsg;
	}
}
