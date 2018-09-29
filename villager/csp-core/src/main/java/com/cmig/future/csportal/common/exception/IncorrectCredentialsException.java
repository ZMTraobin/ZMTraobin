package com.cmig.future.csportal.common.exception;

/**
 * Created by zhangtao107@126.com on 2017/3/21.
 */
public class IncorrectCredentialsException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public IncorrectCredentialsException(){

    }

    public IncorrectCredentialsException(String message){
        super(message);
    }
}
