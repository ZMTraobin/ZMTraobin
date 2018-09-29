package com.cmig.future.csportal.common.exception;

/**
 * Created by zhangtao107@126.com on 2017/3/21.
 */
public class UnknownAccountException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnknownAccountException(){

    }

    public UnknownAccountException(String message){
        super(message);
    }
}
