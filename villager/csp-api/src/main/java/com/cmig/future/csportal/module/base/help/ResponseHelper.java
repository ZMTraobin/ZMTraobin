package com.cmig.future.csportal.module.base.help;

import com.cmig.future.csportal.common.mapper.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:05 2017/8/24.
 * @Modified by zhangtao on 16:05 2017/8/24.
 */
public class ResponseHelper {

	private static final Logger logger= LoggerFactory.getLogger(ResponseHelper.class);

	/**
	 * 客户端返回JSON字符串
	 *
	 * @param response
	 * @param object
	 * @return
	 */
	public static String renderString(HttpServletResponse response, Object object) {
		//logger.debug(JsonMapper.toJsonString(object));
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}

	/**
	 * 客户端返回字符串
	 *
	 * @param response
	 * @param string
	 * @return
	 */
	public static String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
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
