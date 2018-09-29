package com.cmig.future.csportal.module.sys.interceptor.api;

import com.cmig.future.csportal.common.mapper.JsonMapper;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author:zhangtao
 * @Description:乐家慧APP token拦截器
 * @Date Created in 9:49 2017/7/5.
 * @Modified by zhangtao on 9:49 2017/7/5.
 */
public class AuthorizationInterceptor implements HandlerInterceptor {

	private Logger logger= LoggerFactory.getLogger(AuthorizationInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		// 从header中得到token
		String token = request.getParameter("token");
		logger.debug("token {}",token);

		if (StringUtils.isEmpty(token)) {
			renderString(response, RetAppUtil.error(new ServiceException(RestApiExceptionEnums.UNLOGIN_EXCEPTION)));
			return false;
		}
		if (!UserTokenUtils.checkToken(token)) {
			renderString(response, RetAppUtil.error(new ServiceException(RestApiExceptionEnums.UNLOGIN_EXCEPTION)));
			return false;
		}
	
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	/**
	 * 客户端返回JSON字符串
	 * 
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		logger.info(JsonMapper.toJsonString(object));
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}

	/**
	 * 客户端返回字符串
	 * 
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
			response.addHeader("Access-Control-Max-Age", "1728000");
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.println(string);
			out.flush();
			out.close();
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
