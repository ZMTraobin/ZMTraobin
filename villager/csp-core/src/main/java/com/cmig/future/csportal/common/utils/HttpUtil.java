package com.cmig.future.csportal.common.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static String charset="UTF-8";

    /**
     * 
     * @Title post
     * @Description 只调用url的post请求
     * @Company cm-lj 
     * @Author 胡文飞
     * @data 2016年3月1日 下午3:36:25
     * @param url
     * @return String
     */
    public static String post(String url) {
    	return post(url,null,null);
    }
    /**
     * 
     * @Title post
     * @Description post raw形式请求
     * @Company cm-lj 
     * @Author 胡文飞
     * @data 2016年3月1日 下午5:51:03
     * @param url
     * @param raw
     * @return String
     */
    public static String post(String url,String raw) {
    	return post(url,raw,null);
    }

    /**
     * post json 入参
     * @param url
     * @param jsonObject
     * @return
     */
    public static String post(String url,JSONObject jsonObject) {
        return post(url,jsonObject,null);
    }

    /**
     * 
     * @Title post
     * @Description 带参数的post请求
     * @Company cm-lj 
     * @Author 胡文飞
     * @data 2016年3月1日 下午3:37:17
     * @param url
     * @param formparams
     * @return String
     */
    public static String post(String url, List<BasicNameValuePair> formparams) {
    	return post(url,formparams,null);
    }

    /**
     *
     * @Title post
     * @Description 带参数的post请求
     * @Company cm-lj
     * @Author 胡文飞
     * @data 2016年3月1日 下午3:37:17
     * @param url
     * @param paramsMap
     * @return String
     */
    public static String post(String url, Map paramsMap) {
        List<BasicNameValuePair> list=new ArrayList<BasicNameValuePair>();
        if(null!=paramsMap){
            Set<Map.Entry<String, String>> entrySet = paramsMap.entrySet();
            for(Map.Entry<String, String> entry : entrySet){
                list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
        }
        return post(url,list,null);
    }
    /**
     *  
     * @Title post
     * @Description 带参数以及头部信息的post请求
     * @Company cm-lj 
     * @Author 胡文飞
     * @data 2016年3月1日 下午3:38:21
     * @param url
     * @param formparams
     * @param headers
     * @return String
     */
    public static String post(String url, Object formparams,Map<String, String> headers) {  
    	String responseStr = null;
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
        UrlEncodedFormEntity uefEntity;
        try {
            //设置参数
        	if(formparams!=null){
        		if(formparams instanceof List<?>){
        			List<BasicNameValuePair> formNameValue = (List<BasicNameValuePair>)formparams;
			        logger.debug("post type {} params {} ","form",formNameValue.toString());

                    //下面这一行是对字符进行urlEncode，会把中文转换成%xx%xx%xx这样的形式。
        			uefEntity = new UrlEncodedFormEntity(formNameValue, "UTF-8");
                    httppost.setEntity(uefEntity);
        		}else if(formparams instanceof JSONObject){
			        JSONObject jsonObject = (JSONObject) formparams;
                    logger.debug("post type {} params {}","JSONObject",jsonObject.toString());
                    StringEntity s = new StringEntity(jsonObject.toString());
                    s.setContentEncoding("UTF-8");
                    s.setContentType("application/json");//发送json数据需要设置contentType
                    httppost.setEntity(s);
                }else if(formparams instanceof String){
			        String rawString = (String) formparams;
        			logger.debug("post type {} params {}","raw",rawString);
        			httppost.setEntity(new StringEntity(rawString, "UTF-8"));
        		}else{
        			logger.error("参数类型错误!");
        			throw new ClassCastException("参数类型错误!");
        		}
        	}
        	//设置头信息
        	if(headers!=null){
        		Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        		for(Map.Entry<String, String> entry : entrySet){
        			httppost.addHeader(entry.getKey(),entry.getValue());
        		}
        	}

            logger.debug("executing request {} " , httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                	responseStr = EntityUtils.toString(entity, "UTF-8");
                	logger.debug("--------------------------------------");
                	logger.debug("Response content {} ", responseStr);
                	logger.debug("--------------------------------------");
                    
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        }
        return responseStr;
    }

	/**
	 * 发送 get请求
 	 * @param url
	 * @return
	 */
	public static String get(String url) throws Exception {
	    return get(url,null,null);
    }

	/**
	 * 发送 get请求
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String get(String url,Map<String, String> headers) throws Exception {
        return get(url,null,headers);
    }

    /**
     * 发送 get请求
     * @param url
     * @param paramsMap
     * @param headers
     * @return
     * @throws Exception
     */
    public static String get(String url,Map<String,String> paramsMap,Map<String, String> headers) throws Exception{
	    String responseStr=null;
        //创建默认的httpclient实例
        CloseableHttpClient httpclient = null;
        try{
            httpclient = HttpClients.createDefault();
            //创建参数队列
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            if(paramsMap !=null && !paramsMap.isEmpty()){
                for(Map.Entry<String,String>entry:paramsMap.entrySet()){
                    params.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                }

	            if(url.indexOf("?")<0){
		            url += "?" ;
	            }
	            url +=EntityUtils.toString(new UrlEncodedFormEntity(params, charset));
            }
            HttpGet httpget=new HttpGet(url);
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();
            httpget.setConfig(requestConfig);

            //设置头信息
            if(headers!=null&& !headers.isEmpty()){
                Set<Map.Entry<String, String>> entrySet = headers.entrySet();
                for(Map.Entry<String, String> entry : entrySet){
                    httpget.addHeader(entry.getKey(),entry.getValue());
                }
            }

	        logger.debug("executing request {} " , httpget.getURI());
	        // 执行get请求.
	        CloseableHttpResponse response = httpclient.execute(httpget);
	        try {
		        // 获取响应实体
		        HttpEntity entity = response.getEntity();
		        logger.debug("--------------------------------------");
		        // 打印响应状态
		        logger.debug("Response status {}" ,String.valueOf(response.getStatusLine()));
		        if (entity != null) {
			        // 打印响应内容长度
			        logger.debug("Response content length {}" , entity.getContentLength());
			        responseStr = EntityUtils.toString(entity,"utf-8");
			        // 打印响应内容
			        logger.debug("Response content {} " , responseStr);
		        }
		        logger.debug("------------------------------------");
	        } finally {
		        response.close();
	        }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpclient.close();
        }
        return responseStr;
    }

}
