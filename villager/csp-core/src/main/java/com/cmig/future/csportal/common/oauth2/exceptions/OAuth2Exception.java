package com.cmig.future.csportal.common.oauth2.exceptions;

import com.cmig.future.csportal.module.base.enums.ExceptionEnum;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;

public class OAuth2Exception extends ServiceException {


	public OAuth2Exception(ExceptionEnum e) {
		super(e);
	}

	public OAuth2Exception(ExceptionEnum e, String extMsg) {
		super(e, extMsg);
	}
}
