package com.cmig.future.csportal.module.properties.company.exception;

import com.hand.hap.core.exception.BaseException;

public class CompanyException extends BaseException {

    public CompanyException(String code, String descriptionKey, Object[] parameters) {
        super(code, descriptionKey, parameters);
    }

    public static final String MSG_ERROR_CORPID_NOT_NULL = "msg.error.company.corpid.notnull";


}
