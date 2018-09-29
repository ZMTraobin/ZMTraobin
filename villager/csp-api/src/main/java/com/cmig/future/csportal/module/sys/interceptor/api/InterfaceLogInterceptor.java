/**
 * .
 */
package com.cmig.future.csportal.module.sys.interceptor.api;

import com.cmig.future.csportal.common.mapper.JsonMapper;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.sys.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * @Author:zhangtao
 * @Description:api日期记录拦截器
 * @Date Created in 9:49 2017/7/5.
 * @Modified by zhangtao on 9:49 2017/7/5.
 */
public class InterfaceLogInterceptor implements HandlerInterceptor {

	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");
    private Logger logger= LoggerFactory.getLogger(InterfaceLogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(request.getRequestURI().endsWith("common/ping/test")){
			return true;
		}

		long beginTime = System.currentTimeMillis();// 1、开始时间
        startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
		if (logger.isDebugEnabled()) {
			logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS").format(beginTime), request.getRequestURI());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			logger.info("ViewName: " + modelAndView.getViewName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

		if(request.getRequestURI().endsWith("common/ping/test")){
			return ;
		}

		long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long endTime = System.currentTimeMillis(); // 2、结束时间
			
		
		long time = endTime-beginTime;
		// 保存日志
		
		LogUtils.saveLog(request, handler, ex, time);

		// 打印JVM信息。
		if (logger.isDebugEnabled()) {
			logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m", new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), DateUtils.formatDateTime(endTime - beginTime), request.getRequestURI(), Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024, Runtime.getRuntime().freeMemory() / 1024 / 1024, (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
		}

	}

	/**
	 * 客户端返回JSON字符串
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
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
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
