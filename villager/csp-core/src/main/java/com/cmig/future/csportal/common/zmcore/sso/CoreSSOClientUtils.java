package com.cmig.future.csportal.common.zmcore.sso;

/**
 * Created by zhangtao107@126.com on 2016/8/24.
 */


import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.zmcore.usercenter.CoreUserUtils;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CoreSSOClientUtils {

    private static Logger log = LoggerFactory.getLogger(CoreSSOClientUtils.class);
/*    private static String CORE_SSO_URL_TGT = "http://10.17.5.86/cas/v1/tickets";
    private static String CORE_SSO_URL_ST = "http://10.17.5.86/cas/v1/tickets";
    private static String CORE_SSO_URL_UUID = "http://10.17.5.86/cas/proxyValidate";
    private static String CORE_SSO_URL_TKA="http://10.17.5.86/cas/v1/ticketsKeepAlive";
    private static String loginOutNotifyUrl="http://localhost:8081/ljhui-api/user/loginOut/";*/



    private static String CORE_SSO_URL_TGT;
    private static String CORE_SSO_URL_ST;
    private static String CORE_SSO_URL_UUID;
    private static String CORE_SSO_URL_TKA;
    private static String loginOutNotifyUrl="http://localhost:8081/ljhui-api/user/loginOut/";

	private static String CORE_SSO_URL_THIRD_TGT;
	private static String CORE_SSO_URL_THIRD_BIND;
	private static String CORE_SSO_URL_THIRD_QUERY;
	private static String CORE_SSO_URL_THIRD_GET_WXOPENID;



    static{
	    String serverUrl=Global.getHspServerUrl("CORE_SSO_URL");
        CORE_SSO_URL_TGT= serverUrl+"/cas/v1/tickets";
        CORE_SSO_URL_ST= serverUrl+"/cas/v1/tickets";
        CORE_SSO_URL_UUID= serverUrl+"/cas/proxyValidate";
        CORE_SSO_URL_TKA= serverUrl+"/cas/v1/ticketsKeepAlive";
	    CORE_SSO_URL_THIRD_TGT= serverUrl+"/cas/v1/third/tickets";
	    CORE_SSO_URL_THIRD_BIND= serverUrl+"/cas/v1/third/bind";
	    CORE_SSO_URL_THIRD_QUERY= serverUrl+"/cas/v1/third/query";
	    CORE_SSO_URL_THIRD_GET_WXOPENID= serverUrl+"/cas/v1/third/getWxOpenId";
    }


	/**
	 * 用户名密码登录
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
    public static Map<String ,String> getUuid(String username, final String password) throws Exception {
	    String tgt=getTicketGrantingTicket( username, password, null);
	    String st=getServiceTicket(tgt);
	    String uid=proxyValidate(st);

	    Map map=new HashMap();
	    map.put("tgt",tgt);
	    map.put("st",st);
	    map.put("uid",uid);
	    return map;
    }

	/**
	 * 第三方账户联合登录
	 * @param identityType
	 * @param appId
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static Map<String ,String> thirdLogin(String identityType,String appId, String code) throws Exception {
		String tgt=getThirdTicketGrantingTicket(identityType, appId, code);
		String st=getServiceTicket(tgt);
		String uid=proxyValidate(st);

		Map map=new HashMap();
		map.put("tgt",tgt);
		map.put("st",st);
		map.put("uid",uid);
		return map;
	}

	/**
	 * 取得ST
	 * @param ticketGrantingTicket
	 * @return
	 * @throws Exception
	 */
	public static String getServiceTicket(final String ticketGrantingTicket) throws Exception {
        if (ticketGrantingTicket == null) {
            return null;
        }
        final HttpClient client = new HttpClient();
        final PostMethod post = new PostMethod(CORE_SSO_URL_ST + "/" + ticketGrantingTicket);
        try {
            notNull(CORE_SSO_URL_ST, "server must not be null");
            notNull(ticketGrantingTicket, "ticketGrantingTicket must not be null");
            notNull(loginOutNotifyUrl, "service must not be null");

	        post.addRequestHeader("Authorization", Global.getHspAuthorization());
            post.setRequestBody(new NameValuePair[]{new NameValuePair("service", loginOutNotifyUrl)});
            client.executeMethod(post);
            final String response = post.getResponseBodyAsString();
            log.debug("statusCode {} , getServiceTicket {}" ,post.getStatusCode(), response);
            switch (post.getStatusCode()) {
                case 200:
                    return response;
                default:
                    log.info("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
                    log.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                    break;
            }
        }catch (DataWarnningException e) {
            log.error(e.getMessage());
            throw  new DataWarnningException(e.getMessage());
        }catch (Exception e) {
            log.error(e.getMessage());
            throw  new Exception(e.getMessage());
        } finally {
	        if(null!=post) {
		        post.releaseConnection();
	        }
        }
        return null;
    }

	/**
	 * 取得TGT
	 * @param username
	 * @param password
	 * @param identifyType
	 * @return
	 * @throws Exception
	 */
	public static String getTicketGrantingTicket(final String username, final String password, final String identifyType) throws Exception {
        final HttpClient client = new HttpClient();
        final PostMethod post = new PostMethod(CORE_SSO_URL_TGT);

        try {
            notNull(CORE_SSO_URL_TGT, "server must not be null");
            notNull(username, "username must not be null");
            notNull(password, "password must not be null");

	        post.addRequestHeader("Authorization", Global.getHspAuthorization());
            post.setRequestBody(new NameValuePair[]{
                    //new NameValuePair("identifyType", identifyType),
                    new NameValuePair("username", username),
                    new NameValuePair("password", password)});

            client.executeMethod(post);
            final String response = post.getResponseBodyAsString();
	        log.debug("statusCode {} , TGT {}" ,post.getStatusCode(), response);
            switch (post.getStatusCode()) {
                case 201: {
                    final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);
                    if (matcher.matches()) {
                        return matcher.group(1);
                    }
                    log.warn("Successful ticket granting request, but no ticket found!");
                    log.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
                    break;
                }

                default:

                    log.warn("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
                    log.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));

                    JSONObject jsonObject=JSONObject.fromObject(response);
                    String code=JSONObject.fromObject(jsonObject.getString("Resp")).getString("code");
                    if("0018".equals(code)){
                        String failedTryTimes=JSONObject.fromObject(jsonObject.getString("Resp")).getString("failedTryTimes");
                        String remainTryTimes=JSONObject.fromObject(jsonObject.getString("Resp")).getString("remainTryTimes");
                        int total= new Integer(failedTryTimes)+new Integer(remainTryTimes);
                        throw new LoginFailException(CoreUserUtils.errorCoeMap.get(code).toString()+"，连续错误"+total+"次后锁定，已错误"+failedTryTimes+"次");
                    }else{
                        //String failedTryTimes=JSONObject.fromObject(jsonObject.getString("Resp")).getString("failedTryTimes");
                        //String remainTryTimes=JSONObject.fromObject(jsonObject.getString("Resp")).getString("remainTryTimes");
                        throw new DataWarnningException(CoreUserUtils.errorCoeMap.get(code)==null?"登录失败":CoreUserUtils.errorCoeMap.get(code).toString());
                     }
            }
        }catch (LoginFailException e) {
            log.error(e.getMessage());
            throw  new LoginFailException(e.getMessage());
        }catch (DataWarnningException e) {
            log.error(e.getMessage());
            throw  new DataWarnningException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw  new Exception(e.getMessage());
        } finally {
	        if(null!=post) {
		        post.releaseConnection();
	        }
        }
        return null;
    }

    /**
     * 延时
     * @param serviceTicket
     * @param ticketGrantingTicket
     * @param timeout
     */
    public static void ticketsKeepAlive(String serviceTicket, String ticketGrantingTicket,int timeout) {
        KeepAliveTask keepAliveTask=new KeepAliveTask(timeout,CORE_SSO_URL_TKA,ticketGrantingTicket,serviceTicket);
        keepAliveTask.start();
    }

	/**
	 *
	 * 获取用户id
	 * @param serviceTicket
	 * @return
	 * @throws Exception
	 */
	public static String proxyValidate( String serviceTicket) throws Exception {
        final HttpClient client = new HttpClient();
        GetMethod post = null;
        try {
            notNull(CORE_SSO_URL_UUID, "paramter 'serverValidate' is not null");
            notNull(serviceTicket, "paramter 'serviceTicket' is not null");
            notNull(loginOutNotifyUrl, "paramter 'service' is not null");

            post = new GetMethod(CORE_SSO_URL_UUID + "?" + "ticket=" + serviceTicket + "&service=" + URLEncoder.encode(loginOutNotifyUrl, "UTF-8"));
	        post.addRequestHeader("Authorization", Global.getHspAuthorization());
            log.info(CORE_SSO_URL_UUID + "?" + "ticket=" + serviceTicket + "&service=" + URLEncoder.encode(loginOutNotifyUrl, "UTF-8"));
            client.executeMethod(post);
            final String response = post.getResponseBodyAsString();
	        log.debug("statusCode {} , response {}" ,post.getStatusCode(), response);
            switch (post.getStatusCode()) {
                case 200: {
                    final Matcher matcher = Pattern.compile(".*<cas:uid>(.*)</cas:uid>.*").matcher(response);
                    if (matcher.find()) {
                        log.info("成功取得用户数据");
                        log.info(matcher.group(1));
                        return matcher.group(1);
                    }
                }
                default: {

                }
            }
        }catch (DataWarnningException e) {
            log.error(e.getMessage());
            throw  new DataWarnningException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw  new Exception(e.getMessage());
        } finally {
            //释放资源
	        if(null!=post) {
		        post.releaseConnection();
	        }
        }
        return null ;
    }


	/**
	 * 取得TGT
	 * @param thirdChannelCode
	 * @param appId
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String getThirdTicketGrantingTicket(final String thirdChannelCode,final String appId,  final String code) throws Exception {
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(CORE_SSO_URL_THIRD_TGT);

		try {
			notNull(thirdChannelCode, "thirdChannelCode must not be null");
			notNull(appId, "appId must not be null");
			notNull(code, "code must not be null");

			post.addRequestHeader("Authorization", Global.getHspAuthorization());
			post.setRequestBody(new NameValuePair[]{
					new NameValuePair("thirdChannelCode", thirdChannelCode),
					new NameValuePair("appId", appId),
					new NameValuePair("code", code)});

			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			log.debug("statusCode {} , TGT {}" ,post.getStatusCode(), response);
			switch (post.getStatusCode()) {
				case 201: {
					final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);
					if (matcher.matches()) {
						return matcher.group(1);
					}
					log.warn("Successful ticket granting request, but no ticket found!");
					log.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
					break;
				}

				default:

					log.warn("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
					log.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));

					JSONObject jsonObject=JSONObject.fromObject(response);
					String responCode=JSONObject.fromObject(jsonObject.getString("Resp")).getString("code");
					String message=JSONObject.fromObject(jsonObject.getString("Resp")).getString("message");
					if("10011".equals(responCode)){
						throw new ServiceException(RestApiExceptionEnums.THIRD_CHANNEL_OAUTH_ERROR,message);
					}else if("10016".equals(responCode)){
						throw new ServiceException(RestApiExceptionEnums.UUID_LOGIN_FAIL);
					}else if("0018".equals(responCode)){
						String failedTryTimes=JSONObject.fromObject(jsonObject.getString("Resp")).getString("failedTryTimes");
						String remainTryTimes=JSONObject.fromObject(jsonObject.getString("Resp")).getString("remainTryTimes");
						int total= new Integer(failedTryTimes)+new Integer(remainTryTimes);
						throw new LoginFailException(CoreUserUtils.errorCoeMap.get(responCode).toString()+"，连续错误"+total+"次后锁定，已错误"+failedTryTimes+"次");
					}else{
						throw new DataWarnningException(CoreUserUtils.errorCoeMap.get(responCode)==null?"登录失败":CoreUserUtils.errorCoeMap.get(responCode).toString());
					}
			}
		}catch (ServiceException e) {
			log.error(e.getMessage());
			throw  e;
		}catch (LoginFailException e) {
			log.error(e.getMessage());
			throw e;
		}catch (DataWarnningException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		} finally {
			if(null!=post) {
				post.releaseConnection();
			}
		}
		return null;
	}

	public static void thirdBind(final String thirdChannelCode, final String appId, final String code, final String uid) throws Exception {
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(CORE_SSO_URL_THIRD_BIND);
		try {
			notNull(thirdChannelCode, "thirdChannelCode must not be null");
			notNull(appId, "appId must not be null");
			notNull(code, "code must not be null");
			notNull(uid, "uid must not be null");

			post.addRequestHeader("Authorization", Global.getHspAuthorization());
			post.setRequestBody(new NameValuePair[]{
					new NameValuePair("thirdChannelCode", thirdChannelCode),
					new NameValuePair("appId", appId),
					new NameValuePair("code", code),
					new NameValuePair("uid", uid)});

			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			log.debug("statusCode {} , getServiceTicket {}" ,post.getStatusCode(), response);
			switch (post.getStatusCode()) {
				case 200:
					log.debug(" bind success thirdChannelCode:{} appId:{} code:{} uid:{} ",thirdChannelCode,appId,code,uid);
					break;
				default:
					JSONObject jsonObject=JSONObject.fromObject(response);
					String message=JSONObject.fromObject(jsonObject.getString("Resp")).getString("message");
					throw new DataWarnningException(message);
			}
		}catch (DataWarnningException e) {
			log.error(e.getMessage());
			throw  new DataWarnningException(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
			throw  new Exception(e.getMessage());
		} finally {
			if(null!=post) {
				post.releaseConnection();
			}
		}
	}

	public static String thirdQuery(final String thirdChannelCode, final String appId, final String code) throws Exception {
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(CORE_SSO_URL_THIRD_QUERY);
		try {
			notNull(thirdChannelCode, "thirdChannelCode must not be null");
			notNull(appId, "appId must not be null");
			notNull(code, "code must not be null");

			post.addRequestHeader("Authorization", Global.getHspAuthorization());
			post.setRequestBody(new NameValuePair[]{
					new NameValuePair("thirdChannelCode", thirdChannelCode),
					new NameValuePair("appId", appId),
					new NameValuePair("code", code)});

			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			log.debug("statusCode {} , getServiceTicket {}" ,post.getStatusCode(), response);
			JSONObject jsonObject=JSONObject.fromObject(response);
			switch (post.getStatusCode()) {
				case 200:
					String uid=JSONObject.fromObject(jsonObject.getString("Resp")).getString("uid");
					return uid;
				default:
					String message=JSONObject.fromObject(jsonObject.getString("Resp")).getString("message");
					throw new DataWarnningException(message);
			}
		}catch (DataWarnningException e) {
			log.error(e.getMessage());
			throw  new DataWarnningException(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
			throw  new Exception(e.getMessage());
		} finally {
			if(null!=post) {
				post.releaseConnection();
			}
		}
	}


	public static String thirdGetWxOpenId(final String thirdChannelCode, final String appId, final String code) throws Exception {
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(CORE_SSO_URL_THIRD_GET_WXOPENID);
		try {
			notNull(thirdChannelCode, "thirdChannelCode must not be null");
			notNull(appId, "appId must not be null");
			notNull(code, "code must not be null");

			post.addRequestHeader("Authorization", Global.getHspAuthorization());
			post.setRequestBody(new NameValuePair[]{
					new NameValuePair("thirdChannelCode", thirdChannelCode),
					new NameValuePair("appId", appId),
					new NameValuePair("code", code)});

			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			log.debug("statusCode {} , getServiceTicket {}" ,post.getStatusCode(), response);
			JSONObject jsonObject=JSONObject.fromObject(response);
			switch (post.getStatusCode()) {
				case 200:
					String openId=JSONObject.fromObject(jsonObject.getString("Resp")).getString("uid");
					return openId;
				default:
					String message=JSONObject.fromObject(jsonObject.getString("Resp")).getString("message");
					throw new DataWarnningException(message);
			}
		}catch (DataWarnningException e) {
			log.error(e.getMessage());
			throw  new DataWarnningException(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
			throw  new Exception(e.getMessage());
		} finally {
			if(null!=post) {
				post.releaseConnection();
			}
		}
	}

    public static void notNull(final Object object, final String message) throws DataWarnningException {
        if (StringUtils.isEmpty(object)) {
            throw new DataWarnningException("参数错误 ");
        }
    }

    public static void main(final String[] args) throws Exception {
        final String username = "18210225831";
        final String password = "peony07";

        long start = System.currentTimeMillis();
        String ticketGrantingTicket = getTicketGrantingTicket(username, password, null);
        long end = System.currentTimeMillis();
        log.info("getTicketGrantingTicket inteval:" + (end - start) / 1000.00);
        start = System.currentTimeMillis();
        String serviceTicket = getServiceTicket(ticketGrantingTicket);
        end = System.currentTimeMillis();
        log.info("getServiceTicket inteval:" + (end - start) / 1000.00);
        start = System.currentTimeMillis();
        proxyValidate(serviceTicket);
        end = System.currentTimeMillis();
        log.info("ticketValidate inteval:" + (end - start) / 1000.00);

        ticketsKeepAlive(serviceTicket,ticketGrantingTicket,7*24*3600);

        log.info("finished!!!");
    }
}
