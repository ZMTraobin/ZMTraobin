package com.cmig.future.csportal.module.base.exceptions;

import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 9:49 2017/7/5.
 * @Modified by zhangtao on 9:49 2017/7/5.
 */
public class ServiceExceptionHelper extends RuntimeException {

    public static ServiceException argsNull() {
        return new ServiceException(RestApiExceptionEnums.ARGS_NULL);
    }

    public static ServiceException argsNull(String extMsg) {
        return new ServiceException(RestApiExceptionEnums.ARGS_NULL, extMsg);
    }

    public static ServiceException argsError() {
        return new ServiceException(RestApiExceptionEnums.ARGS_ERROR);
    }

    public static ServiceException argsError(String extMsg) {
        return new ServiceException(RestApiExceptionEnums.ARGS_ERROR, extMsg);
    }
}
