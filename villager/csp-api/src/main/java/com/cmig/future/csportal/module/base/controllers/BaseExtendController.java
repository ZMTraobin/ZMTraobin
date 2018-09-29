package com.cmig.future.csportal.module.base.controllers;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.exceptions.OAuth2Exception;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.zmcore.sso.LoginFailException;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.help.ResponseHelper;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.pay.conf.PingppConfig;
import com.cmig.future.csportal.module.weixin.WorkWxException;
import com.hand.hap.system.controllers.BaseController;
import com.pingplusplus.Pingpp;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器支持类
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseExtendController extends BaseController {

	public static final String OK = "1";
	public static final String FAIL = "0";

    static{
        Pingpp.appId= PingppConfig.appId;
        // 设置 API Key
        Pingpp.apiKey = PingppConfig.apiKey;
        // 设置私钥路径，用于请求签名
        Pingpp.privateKeyPath = PingppConfig.privateKeyFilePath;
    }

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 文件服务器地址
     */
    @Value("${fastdfs.imageServer}")
    protected String fastdfsImageServer;



	/**
	 * 添加Model消息
	 * @param messages
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息
	 * @param messages
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

    /**
     * 此方法描述的是：获取ip
     *
     * @param request
     * @return String
     * @author:zhangtao107@126.com
     */
    public String getRemoteid(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 重写getParameter
     * @param request
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getParam(HttpServletRequest request,
                              String key,String defaultValue) {

        String value = request.getParameter(key);

        if(value==null||value.isEmpty()){
            return defaultValue;
        }
        return StringEscapeUtils.escapeHtml4(value.trim());
    }

    /**
     * 客户端返回JSON字符串
     * @param response
     * @param object
     * @return
     */
    protected String renderString(HttpServletResponse response, Object object) {
        return ResponseHelper.renderString(response,object);
    }

	/**
	 *
	 * @param request
	 * @param param
	 * @throws DataWarnningException
	 */
	public void checkNull(HttpServletRequest request,String param)throws ServiceException{
		String value=getParam(request,param,"");
		if(StringUtils.isEmpty(value)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,param);
		}
	}

	/**
	 *
	 * @param value
	 * @throws DataWarnningException
	 */
	public void checkNull(String value)throws ServiceException{
		if(StringUtils.isEmpty(value)){
			throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
		}
	}

	/**
	 * 统一异常处理
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler({Exception.class})
	public Object exceptionHandler(Exception ex, HttpServletRequest request) {
		RetApp retApp=new RetApp();
		if (ex instanceof DataWarnningException || ex instanceof LoginFailException) {
			retApp.setMessage(ex.getMessage());
			retApp.setStatus(FAIL);
		}else if (ex instanceof OAuth2Exception) {
			retApp= RetAppUtil.error((OAuth2Exception)ex);
		}else if (ex instanceof ServiceException) {
			retApp= RetAppUtil.error((ServiceException)ex);
		}else if (ex instanceof WorkWxException) {
			retApp.setMessage(ex.getMessage());
			retApp.setStatus(FAIL);
		}else {
			this.logger.error(ex.getMessage(), ex);
			retApp= RetAppUtil.unKonwError();
		}
		return retApp;
	}

}
