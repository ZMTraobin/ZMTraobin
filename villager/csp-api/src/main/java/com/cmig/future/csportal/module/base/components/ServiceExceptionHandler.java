package com.cmig.future.csportal.module.base.components;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cmig.future.csportal.module.base.controllers.BaseExtendController.FAIL;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 19:36 2017/7/5.
 * @Modified by zhangtao on 19:36 2017/7/5.
 */
public class ServiceExceptionHandler implements HandlerExceptionResolver {

	private static final Logger logger= LoggerFactory.getLogger(ServiceExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request,
	                                     HttpServletResponse response,
	                                     Object handler,
	                                     Exception ex) {
		RetApp retApp=new RetApp();
		if (ex instanceof DataWarnningException) {
			retApp.setMessage(ex.getMessage());
			retApp.setStatus(FAIL);
		}else if (ex instanceof OAuth2Exception) {
			retApp= RetAppUtil.error((ServiceException)ex);
		}else if (ex instanceof ServiceException) {
			retApp= RetAppUtil.error((ServiceException)ex);
		}else {
			retApp= RetAppUtil.unKonwError();
		}
		responseResult(response,retApp);
		return new ModelAndView();
	}

	private void responseResult(HttpServletResponse response, RetApp retApp) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "application/json;charset=UTF-8");
		response.setStatus(200);
		try {
			response.getWriter().write(JSONObject.toJSONString(retApp));
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
}
