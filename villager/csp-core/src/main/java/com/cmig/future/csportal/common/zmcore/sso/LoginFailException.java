package com.cmig.future.csportal.common.zmcore.sso;

/**
 * Created by zhangtao107@126.com on 2016/9/28.
 */
public class LoginFailException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public LoginFailException(){

    }

    public LoginFailException(String message){
        super(message);
    }
}
