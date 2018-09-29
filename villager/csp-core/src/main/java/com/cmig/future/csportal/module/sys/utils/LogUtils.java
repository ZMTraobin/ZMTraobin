/**
 * .
 */
package com.cmig.future.csportal.module.sys.utils;

import com.cmig.future.csportal.common.utils.Exceptions;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.sys.apilog.dto.SysLog;
import com.cmig.future.csportal.module.sys.apilog.mapper.SysLogMapper;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author ThinkGem
 * @version 2014-11-7
 */
public class LogUtils {
    private static SysLogMapper logDao = SpringContextHolder.getBean(SysLogMapper.class);

	private static final Logger logger= LoggerFactory.getLogger(LogUtils.class);

    /**
     * 保存日志api
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, long time) {
        SysLog log = new SysLog();
        String token = request.getParameter("token");
	    String ip = getRemoteid(request);
        log.setRemoteAddr(ip);
        String userAgent = request.getHeader("user-agent");
        log.setUserAgent(userAgent);
        log.setRequestUri(request.getRequestURI());

        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) request.getParameterMap()).entrySet()) {
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        log.setParams(params.toString());
        log.setMethod(request.getMethod());
        log.setAppId(request.getHeader("appId"));//业主端：appNameOwner；物业端：appNameProperty
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(log.getAppId())) {
            if (log.getAppId().equalsIgnoreCase("appNameOwner")) {
                log.setAppUserId(UserTokenUtils.getUserIdByToken(token));
            } else {
                log.setAppUserId(AdminTokenUtils.getUserIdByToken(token));
            }
        }

        log.setDeviceType(request.getHeader("platform"));//终端类型 iOS：1；Android：2；wap：3
        log.setDeviceModel(request.getHeader("devmanufacturer"));//终端厂商
        log.setEquipmentModel(request.getHeader("devmodel"));//设备型号
        log.setRemoteIp(request.getHeader("devIP"));//终端的IP地址

        log.setSystemVersion(request.getHeader("devosversion"));//终端系统版本号
        log.setMacAddress(request.getHeader("devmac"));//MAC地址
        log.setImei(request.getHeader("devid"));//IEMI(国际移动设备标识)
        log.setRespTime((long) time);//接口耗时
        log.setLocation(request.getHeader("devsite"));//经度,纬度
        log.setNettype(request.getHeader("devnet"));//网络类型
        String devoperators = request.getHeader("devoperators");//运营商
        try {
            if (!StringUtils.isEmpty(devoperators)) {
                devoperators = URLDecoder.decode(devoperators, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.setDevoperators(devoperators);
        log.setAppVersion(request.getHeader("appVersion"));//app版本号
        // 异步保存日志
        new SaveLogThread(log, handler, ex).start();
    }

	/**
	 * 此方法描述的是：获取ip
	 *
	 * @param request
	 * @return String
	 * @author:zhangtao107@126.com
	 */
	public static String getRemoteid(HttpServletRequest request) {
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
     * 保存日志线程
     */
    public static class SaveLogThread extends Thread {

        private SysLog log;
        private Object handler;
        private Exception ex;

        public SaveLogThread(SysLog log, Object handler, Exception ex) {
            super(SaveLogThread.class.getSimpleName());
            this.log = log;
            this.handler = handler;
            this.ex = ex;
        }

        @Override
        public void run() {
            // 如果有异常，设置异常信息
            log.setException(Exceptions.getStackTraceAsString(ex));
            // 保存日志信息
            log.setCreateTime(new Date());
	        //logger.debug("logJson->"+ JSON.toJSONString(log, SerializerFeature.WriteMapNullValue));
            logDao.insertSelective(log);
        }
    }


}
