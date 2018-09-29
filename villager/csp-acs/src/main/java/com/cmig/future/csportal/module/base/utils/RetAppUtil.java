package com.cmig.future.csportal.module.base.utils;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;

/**
 * @Author:zhangtao
 * @Description:响应结果生成工具
 * @Date Created in 9:35 2017/7/5.
 * @Modified by zhangtao on 9:35 2017/7/5.
 */
public class RetAppUtil {


	/**
	 * 成功
	 * @param object
	 * @param message
	 * @return
	 */
	public static RetApp success(Object object,String message){
		RetApp retApp=new RetApp();
		retApp.setStatus(RestApiExceptionEnums.SUCCESS.getCode().toString());
		retApp.setData(object);
		retApp.setMessage(StringUtils.isEmpty(message)?RestApiExceptionEnums.SUCCESS.getMessage():message);
		return retApp;
	}

	/**
	 * 成功无返回数据
	 * @param message
	 * @return
	 */
	public static RetApp success(String message){
		return success(null,message);
	}

	/**
	 * 失败
	 * @param e
	 * @return
	 */
	public static RetApp error(ServiceException e){
		RetApp retApp=new RetApp();
		retApp.setStatus(e.getCode().toString());
		retApp.setData(null);
		retApp.setMessage(e.getMessage());
		return retApp;
	}

	/**
	 * 失败
	 * @param e
	 * @param data
	 * @return
	 */
	public static RetApp error(ServiceException e,Object data){
		RetApp retApp=new RetApp();
		retApp.setStatus(e.getCode().toString());
		retApp.setData(data);
		retApp.setMessage(e.getMessage());
		return retApp;
	}

	/**
	 * 失败-未知异常
	 * @return
	 */
	public static RetApp unKonwError(){
		RetApp retApp=new RetApp();
		retApp.setStatus(RestApiExceptionEnums.UNKNOW_EXCEPTION.getCode().toString());
		retApp.setData(null);
		retApp.setMessage(RestApiExceptionEnums.UNKNOW_EXCEPTION.getMessage());
		return retApp;
	}
}
