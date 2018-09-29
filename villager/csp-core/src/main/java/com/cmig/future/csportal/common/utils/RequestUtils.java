package com.cmig.future.csportal.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * class name: RequestUtils 
 * author: fri
 * date: 2017年3月28日
 * function: 请求工具类
 */
public class RequestUtils {
	
	public final static String RE_MESSAGE = "message";
	public final static String RE_DATA = "data";
	public final static String TO_SUCCESS = "success";
	public final static String TO_ERROR = "error";
	public final static String MT_JSON = "json";
	public final static String MT_XML = "xml";
	public final static String MT_V2 = "v2";
	public final static String GET = "GET";
	public final static String POST = "POST";
	public final static String CORE_URL = "CORE_URL";
	public final static String CORE1 = "CORE1";
	public final static String CORE2 = "CORE2";
	public final static String T_TOKEN = "token";
	public final static String  T_USER_ID= "userId";
	
	/**
	 * class name: commonHttpRequstReturnObj
	 * author: fri
	 * date: 2017年3月30日
	 * function: 用户中心统一返回接口数据
	 * in:
	 *		parameter1: requetMethod 请求方式[json、xml、V2]
	 *		parameter2: method POST/GET
	 *		parameter3: reParam 必传参数数组[参数必须和对象属性一致]
	 *		parameter4: requestUrlOrName 请求地址或名称
	 *		parameter5: obj 请求参数对象
	 *		parameter6: postJson 根据请求方式决定,不使用则为null
	 *		parameter7: requestParam 
	 * out:
	 *		parameter: JSONObject 统一返回的JSON数据
	 */
	public static JSONObject commonHttpRequstReturnObj(String requetMethod,String method,String[] reParam,String requestUrlOrName,Object obj,JSONObject postJson,Map<String, String> requestParam){
		JSONObject returnJson = new JSONObject(),reJson = new JSONObject();
		String message = TO_SUCCESS;
		Boolean isParamOk = false;
		try {
			isParamOk = isReParamOk(reParam, obj);
			if(isParamOk){
				switch (requetMethod) {
					case MT_JSON:
						if(null != postJson && !postJson.isEmpty()){
							reJson = httpRequst(requestUrlOrName,postJson,requestParam);
						}else{
							message = ExceptionConstants.REQUEST_EXCEPTION.getError();
						}
						break;
					case MT_XML:
						int size = null != reParam ? reParam.length : 0;
						Map<String, String> reMap = new HashMap<>(size);
						if(size > 0)
							reMap = objectToMap(obj);
						reJson = httpRequestXml(requestUrlOrName,reMap,requestParam);//reMap可以为null
						break;
					case MT_V2:
						reJson = httpRequest(requestUrlOrName, method, postJson);
						break;
					default:
						message = ExceptionConstants.REQUEST_EXCEPTION.getError();
						break;
				}
			}else{
				message = ExceptionConstants.DATA_IS_REQUIRED.getError();
			}
		} catch (Exception e) {
			message = ExceptionConstants.OBJ_IS_CHECK_FAILED.getError();//参数与属性一致
		}finally {
			returnJson.put(RE_MESSAGE, message);
			returnJson.put(RE_DATA, reJson);
		}
		return returnJson;
	}
	
	/**
	 * class name: getTokenFromTgt
	 * author: fri
	 * date: 2017年4月15日
	 * function: 通过TGT获取token/userId
	 * in:
	 *		parameter1: requestUrl
	 *		parameter2: requestMethod
	 *		parameter3: jsonObject
	 *			TGT_USERNAME	TGT_PASSWORD	TGT_CLIENT_ID	TGT_CLIENT_SECRET	TGT_SOURCE	TGT_SOURCE	TGT
	 * out:
	 *		parameter: JSONObject
	 */
	public static JSONObject getTokenFromTgt(String requestUrl,String str){
		JSONObject reJson = null;
		try{
	        CloseableHttpClient client = HttpClients.createDefault();
	        HttpPost request = new HttpPost(requestUrl);  
	        request.addHeader("Content-Type", "application/json;charset=UTF-8");
	        StringEntity se = new StringEntity(str);
	        request.setEntity(se);
	        HttpResponse response = client.execute(request);  
	        if (response.getStatusLine().getStatusCode() == 200 ||response.getStatusLine().getStatusCode() == 201) { 
	        	BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String objJson = String.valueOf(sb).replaceAll("null", "\"\"");
				JSONObject jsonobject = JSONObject.fromObject(objJson);
				if(jsonobject.isNullObject())
					return null;
				String token = String.valueOf(jsonobject.get("access_token"));
				if(token.length() == 0)
					return null;
				JSONObject user = jsonobject.getJSONObject("user");
				if(null == user)
					return null;
				String userId = user.getString("id");
				reJson = new JSONObject();
				reJson.put(T_TOKEN, token);
				reJson.put(T_USER_ID, userId);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		return reJson;
	}
	
	/**
	 * class name: isReParamOk
	 * author: fri
	 * date: 2017年3月31日
	 * function: 公共必传参数校验
	 * in:
	 *		parameter1: reParam 必传参数
	 *		parameter2: obj 请求对象
	 * out:
	 *		parameter: Boolean 通过为true 不通过为false
	 */
	public static <T> Boolean isReParamOk(String[] reParam,Object obj) throws Exception{
		Boolean isOk = true;
		if(null != reParam && null != obj && reParam.length > 0){
			Class<? extends Object> classType = obj.getClass();
			for(String param : reParam){
				char[] key = param.toCharArray();
				key[0] -= 32;
				Method getMethod = classType.getMethod("get"+String.valueOf(key),new Class[] {});
				Object value = getMethod.invoke(obj, new Object[] {});
				if(null == value || StringUtils.isBlank(String.valueOf(value))){
					isOk = false;
					break;
				}
			}
		}
		return isOk;
	}
	
	/**
	 * class name: httpRequestXml
	 * author: fri
	 * date: 2017年3月28日
	 * function: 发起http请求并获取结果开始
	 * in:
	 *		parameter1: requestHeader 接口名称 如:CMAPI0008
	 *		parameter2:	reParm	封装请求参数map
	 * out:
	 *		parameter: JSONObject 响应返回数据
	 */
	private static JSONObject httpRequestXml(String requestHeader,Map<String, String> reParm,Map<String, String> requestParam){
		JSONObject reJson = new JSONObject();
		String core = String.valueOf(requestParam.get(CORE_URL));
		try {
			if(null == requestParam || core.length() == 0){
				reJson.put(TO_ERROR, ExceptionConstants.REQUEST_EXCEPTION);
				return reJson;
			}
	        URLConnection con = new URL(core).openConnection();
	        con.setDoOutput(true);
	        con.setRequestProperty("Cache-Control", "no-cache");
	        con.setRequestProperty("Content-Type", "text/xml");
	        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
	        String write = get_iTouReqXml(requestHeader,reParm);
			out.write(new String(write.getBytes("utf-8")));
	        out.flush();
	        out.close();
	        InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
	        BufferedReader br = new BufferedReader(inputStreamReader);
	        String line = "";
	        StringBuffer buffer = new StringBuffer();
	        for (line = br.readLine(); line != null; line = br.readLine()) {
	        	buffer.append(line);
	        }
	        reJson = xmlToJson(String.valueOf(buffer),"<CMAPI","</CMCORE>");
		}catch (Exception e) {
			e.printStackTrace();
			reJson.put(TO_ERROR, ExceptionConstants.REQUEST_ERROR);
		}
        return reJson;
	}
	
	/**
	 * 
	 * class name: httpRequst
	 * author: fri
	 * date: 2017年3月29日
	 * function: 发起http请求并获取结果开始
	 * in:
	 *		parameter1: requestUrl 请求地址
	 *		parameter2: jsonObject 提交的json数据
	 * out:
	 *		parameter: JSONObject (通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	private static JSONObject httpRequst(String requestUrl,JSONObject jsonObject,Map<String, String> requestParam){
		JSONObject jsonobject = new JSONObject();
		String core = String.valueOf(requestParam.get(CORE_URL));
		String core1 = String.valueOf(requestParam.get(CORE1));
		String core2 = String.valueOf(requestParam.get(CORE2));
		if(null == requestParam ||core.length() == 0 || core1.length() == 0 || core2.length() == 0 ){
			jsonobject.put(TO_ERROR, ExceptionConstants.REQUEST_EXCEPTION);
			return jsonobject;
		}
		HttpPost requestuid = new HttpPost(requestUrl);
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(core1, core2);
		provider.setCredentials(AuthScope.ANY, credentials);
		HttpClient clientuid = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		requestuid.setHeader("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
		requestuid.setHeader("Referer", core);
		requestuid.addHeader("Content-type", "application/json; charset=utf-8");
		requestuid.setHeader("Accept", "application/json");
		StringEntity entityuid = new StringEntity(String.valueOf(jsonObject),"utf-8");// 解决中文乱码问题
		entityuid.setContentEncoding("UTF-8");
		entityuid.setContentType("application/json");
		requestuid.setEntity(entityuid);
		try {
			HttpResponse responseuid = clientuid.execute(requestuid);
			BufferedReader inuid = new BufferedReader(new InputStreamReader(responseuid.getEntity().getContent()));
			StringBuffer buffer = new StringBuffer();
			String lineuid = "";
			String NLuid = System.getProperty("line.separator");
			while ((lineuid = inuid.readLine()) != null) {
				buffer.append(lineuid + NLuid);
			}
			inuid.close();
			String objJson = String.valueOf(buffer).replaceAll("null", "\"\"");
			jsonobject = JSONObject.fromObject(objJson);
		} catch (Exception e) {
			jsonobject.put(TO_ERROR, ExceptionConstants.REQUEST_ERROR);
		}
		return jsonobject;
	}
	
	/**
	 * class name: httpRequest
	 * author: fri
	 * date: 2017年4月7日
	 * function: 发起http请求并获取结果
	 * in:
	 *		parameter1: requestUrl 请求地址
	 *		parameter2: requestMethod 请求方法 GET/POST
	 *		parameter3: jsonObject 提交的json数据
	 * out:
	 *		parameter: JSONObject (通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod,JSONObject jsonObject){
		StringBuffer buffer = new StringBuffer();
		JSONObject reJsonObject = new JSONObject();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			httpUrlConn.setRequestProperty("Content-type", "application/json");
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			if (null != jsonObject) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				StringEntity se = new StringEntity(String.valueOf(jsonObject));
				outputStream.write(se.toString().getBytes("UTF-8"));
				outputStream.close();
			}
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpUrlConn.disconnect();
			String objJson = String.valueOf(buffer).replaceAll("null", "\"\"");
			reJsonObject = JSONObject.fromObject(objJson);
		} catch (Exception e) {
			e.printStackTrace();
			reJsonObject.put(TO_ERROR, ExceptionConstants.REQUEST_ERROR);
		}
		return reJsonObject;
	}
	
	/**
	 * class name: get_iTouReqXml
	 * author: fri
	 * date: 2017年3月28日
	 * function: 解析生成请求XML数据
	 * in:
	 *		parameter1: requestHeader 接口名称 如:CMAPI0008
	 *		parameter2: reParm 封装请求参数map
	 * out:
	 *		parameter: StringBuilder
	 */
	private static String get_iTouReqXml(String requestHeader,Map<String, String> reParm){
		StringBuilder sb = new StringBuilder();
		String nowDate = DateUtils.getDate("yyyymmdd");
		sb.append("<CMCORE xmlns=\"http://www.cm-inv.com/CMINV/2015/10\" ");
        sb.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        sb.append("xsi:schemaLocation=\"http://www.cm-inv.com/CMINV/2015/10 CMCORE1.0.xsd\"> ");
        sb.append("<"+requestHeader+"Rq>");
        sb.append("	<CommonRqHdr>");  
        sb.append("		<SPName>zmit</SPName>");  
        sb.append("		<RqUID>"+UUID.randomUUID()+"</RqUID>");
        sb.append("		<NumTranCode>000823</NumTranCode>");
        sb.append("		<ClearDate>"+nowDate+"</ClearDate>");
        sb.append("		<TranDate>"+nowDate+"</TranDate>");
        sb.append("		<TranTime>151440</TranTime>");
        sb.append("		<ChannelId>0000</ChannelId>");
        sb.append("		<Version>1.0</Version>");
        sb.append("	</CommonRqHdr>");
        if(null != reParm && reParm.size() > 0){
	        for(Iterator<String> car = reParm.keySet().iterator();car.hasNext();){
	        	String param = car.next();
	        	String value = reParm.get(param);
	        	sb.append("<"+param+">"+value+"</"+param+">");
	        }
        }
        sb.append("</"+requestHeader+"Rq>");
        sb.append("</CMCORE>");
        System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
	 * class name: objectToMap
	 * author: fri
	 * date: 2017年3月31日
	 * function: 请求对象转换Map对象
	 * in:
	 *		parameter1: obj 请求参数对象
	 * out:
	 *		parameter: Map<String,String> 返回已有参数对象
	 */
	public static Map<String, String> objectToMap(Object obj) throws Exception {
        if(obj == null)
            return null;
        Map<String, String> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (0 == key.compareToIgnoreCase("class")) {
                continue;
            }
            Method getter = property.getReadMethod();
            String value = null != getter ? String.valueOf(getter.invoke(obj)) : null;
            if(null != getter.invoke(obj))
            	map.put(key, value);
        }
        return map.isEmpty() ? null : map;
    }
	
	/**
	 * class name: objectToJson
	 * author: fri
	 * date: 2017年3月31日
	 * function: 请求对象转换Json对象
	 * in:
	 *		parameter1: obj 请求参数对象
	 * out:
	 *		parameter: JSONObject
	 */
	public static JSONObject objectToJson(Object obj){
		if(obj == null)
		    return null;
		JSONObject json = new JSONObject();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	        for (PropertyDescriptor property : propertyDescriptors) {
	            String key = property.getName();
	            if (0 == key.compareToIgnoreCase("class")) {
	                continue;
	            }
	            Method getter = property.getReadMethod();
	            String value = null != getter ? String.valueOf(getter.invoke(obj)) : null;
	            if(null != getter.invoke(obj))
	            	json.put(key, value);
	        }
		}catch (Exception e) {
			return null;
		}
		return json.isEmpty() ? null : json;
	}
	 
	/**
	 * class name: xmlToJson
	 * author: fri
	 * date: 2017年3月28日
	 * function: 转换该返回XML为JSON
	 * in:
	 *		parameter1: xml 需要转换的核心系统对外接口文档XML
	 *		parameter2: startIndexOf 起始截取字符
	 *		parameter3: endIndexOf 末尾截取字符
	 * out:
	 *		parameter: JSONObject json数据
	 */
	public static JSONObject xmlToJson(String xml,String startIndexOf,String endIndexOf){
		int start = xml.indexOf(startIndexOf),end = xml.indexOf(endIndexOf);
		xml = xml.substring(start, end);
		JSON xmlJson = new XMLSerializer().read(xml);
		String objJson = String.valueOf(xmlJson).replaceAll("null", "\"\"");
		JSONObject json = JSONObject.fromObject(objJson);
		if(json.containsKey("list")){
			JSONObject list = null;
			try{
				list = json.getJSONObject("list");
			}catch(Exception e){
			}finally {
				JSONArray jsonArray = null;
				if(null != list){
					jsonArray = JSONArray.fromObject(list);
				}else{
					jsonArray = json.getJSONArray("list");
				}
				json.put("list", jsonArray);
			}
		}
		return json;
	}
	
	/**
	 * class name: jsonToXml
	 * author: fri
	 * date: 2017年3月28日
	 * function: JSON转换为XML
	 * in:
	 *		parameter1: json 需要转换的JSON数据
	 * out:
	 *		parameter: String 转换后的XML数据
	 */
	public static String jsonToXml(String json){
		JSONObject obj = JSONObject.fromObject(json);
		return String.valueOf(new XMLSerializer().write(obj));
	}
	
	/**
	 * class name: checkArrsValue
	 * author: fri
	 * date: 2017年4月6日
	 * function: 检查数据对象是否包含某值
	 * in:
	 *		parameter1: arrs 字符串数组
	 *		parameter2: value 指定字符
	 * out:
	 *		parameter: boolean 包含为true 不包含为false
	 */
	public static boolean checkArrsValue(String[] arrs, String value) {
		if(arrs.length > 0){
			for (String s : arrs) {
				if (value.equals(s))
					return true;
			}
		}
        return false;
    }
	
    public static void main(String[] args) throws Exception {
//    	CAS获取itou用户uid
    	String tgt = getTestUid_CAS();
    	System.out.println(tgt);
//    	String access_token = "4309fc8860f8c000ba2d627df8b02697a23640a53ad3d08f1073d8aaa9dac029";
//    	String userId = "4976003ed99c4302a4a5c72439bd4546";
//    	String requestUrl1 = RequestUtils.iTOU_V2_USER_INFO+userId+"/userfund?access_token="+access_token;
////    	JSONObject json = new JSONObject();
////    	json.put("", TGT);
////    	json.put("", userId);
//    	httpRequest(requestUrl1, GET, null);
    	
//        String url = "http://10.17.5.62:8082/xmlGateway/service";  
//        httpRequestXml(url,getXmlInfo());
    	
//    	String ss = "b2ebd12a-136b-43wb-s3a1-d9114c481ac2";
//    	System.out.println(ss.length());
//    	System.out.println(UUID.randomUUID());
    	
//    	System.out.println(DateUtils.getDate("yyyymmdd"));
    	
//    	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><CMCORE xmlns=\"http://www.cm-inv.com/CMINV/2015/10\"><CMAPI0008Rs><CommonRsHdr><StatusCode>0000</StatusCode><ServerStatusCode>Success</ServerStatusCode><SPRsUID>8038360</SPRsUID><RqUID>b8dc31c3-3321-47d6-beee-7bb11467b04e</RqUID></CommonRsHdr><list><cardDef><bindingName>270</bindingName><bindingCardNo>6217003690002667301</bindingCardNo><bindingBank>中国建设银行</bindingBank><bindingStatus>VALID</bindingStatus><custId>91c26a31cc2b4cd583fac50953a05619</custId><bankCode>0005</bankCode><reservePhone>13501920270</reservePhone><cityCode>0101</cityCode><cityName>东城区</cityName><provCode>01</provCode><provName>北京市</provName><mainFlag>1</mainFlag><binddingtime>2017-02-21 11:31:29</binddingtime><inchannel>0000</inchannel></cardDef></list></CMAPI0008Rs></CMCORE>";
//    	
//    	System.out.println(xmlToJson(getXmlInfo().toString()));
//    	JSONObject postJson = new JSONObject(),reJson = new JSONObject();
//    	JSONObject json = new JSONObject();
//		json.put("uid", "4976003ed99c4302a4a5c72439bd4546");
//    	postJson.put("UserInfoReq", json);
//    	reJson = RequestUtils.httpRequst(RequestUtils.iTOU_GETUSER_INFO, postJson);
//    	System.out.println(reJson);
    	
    	/*//Map->Object
    	BindBankCardReq bindBankCardReq = new BindBankCardReq();
    	bindBankCardReq.setName("张三");
    	bindBankCardReq.setUserId("");
    	bindBankCardReq.setMobile("15109439308");
		//map转换开始
    	Map<String, String> reParm = new HashMap<>(3);
		reParm = objectToMap(bindBankCardReq);
		System.out.println(reParm+"\n<-----------map转换后");
		//json转换开始
		JSONObject reJson = new JSONObject();
		reJson = objectToJson(bindBankCardReq);
		System.out.println(reJson+"\n<------------json转换后");*/
    	/*//公共必填参数校验
    	UserInfoReq userInfoReq = new UserInfoReq();
    	userInfoReq.setUserId("sadfsd");
//    	userInfoReq.setCardNo("asd");
    	String[] reParam = {"userId","cardNo"};
    	Boolean isOk = isReParamOk(reParam,userInfoReq);
    	System.out.println(isOk);*/
    }
    
    //测试数据通过单点登录获取用户ID
	private static String getTestUid_CAS()throws ClientProtocolException, IOException, URISyntaxException {
    	String uid = null;
    	StringBuffer resb = new StringBuffer("");
    	CloseableHttpClient client = HttpClients.createDefault();
		// 实例化HTTP方法
		HttpPost request = new HttpPost("http://10.17.5.86/cas/v1/tickets");
		request.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
		request.setHeader("Referer", "http://hr.cm-inv.com/eassso/login");
		// 创建名/值组列表
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("solutionName", "eas"));
		parameters.add(new BasicNameValuePair("username", "core270"));// 用户名
		parameters.add(new BasicNameValuePair("password", "12345678q"));// 密码（明文）
		// 创建UrlEncodedFormEntity对象
		UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity(parameters);
		request.setEntity(formEntiry);
		// 执行请求
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201) {
			String locationUrl = response.getLastHeader("Location").getValue();
//			System.out.println("CAS1: " + locationUrl);
			String tgt = locationUrl.substring(locationUrl.indexOf("TGT"));
			resb.append(tgt);
			CloseableHttpClient clientcas2 = HttpClients.createDefault();
			// 实例化HTTP方法
			HttpPost requestcas2 = new HttpPost(locationUrl);
			requestcas2.setHeader("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
			requestcas2.setHeader("Referer", "http://hr.cm-inv.com/eassso/login");
			// 创建名/值组列表
			List<NameValuePair> parameterscas2 = new ArrayList<NameValuePair>();
			parameterscas2.add(new BasicNameValuePair("service", "http://10.17.5.86/cas/v1/tickets"));
			// 创建UrlEncodedFormEntity对象
			UrlEncodedFormEntity formEntirycas2 = new UrlEncodedFormEntity(parameterscas2);
			requestcas2.setEntity(formEntirycas2);
			// 执行请求
			HttpResponse responsecas2 = clientcas2.execute(requestcas2);
			String ST = EntityUtils.toString(responsecas2.getEntity(), "UTF-8");
//			System.out.println("CAS2: " + ST);
			String cas3 = "http://10.17.5.86/cas/proxyValidate?service=http%3A%2F%2F10.17.5.86%2Fcas%2Fv1%2Ftickets&ticket="
					+ ST;
			// HttpClient clientcas3 = new DefaultHttpClient();
			CloseableHttpClient clientcas3 = HttpClients.createDefault();
			// 实例化HTTP方法
			HttpGet requestcas3 = new HttpGet();
			requestcas3.setURI(new URI(cas3));
			HttpResponse responsecas3 = clientcas3.execute(requestcas3);
			BufferedReader in = new BufferedReader(new InputStreamReader(responsecas3.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String content = sb.toString();
//			System.out.println("CAS3:" + content);
			int begin = content.indexOf("<cas:uid>");
			int end = content.indexOf("</cas:uid>");
			uid = content.substring(begin + 9, end);
//			resb.append("\n"+uid);
			System.out.println("UID:" + uid);
			// 实例化HTTP方法
			HttpPost requestuid = new HttpPost("http://10.17.5.62:8880/user/info/getUser/iTou");
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("0000", "itou123456");
			provider.setCredentials(AuthScope.ANY, credentials);
			HttpClient clientuid = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
			requestuid.setHeader("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
			requestuid.setHeader("Referer", "http://hr.cm-inv.com/eassso/login");
			requestuid.addHeader("Content-type", "application/json; charset=utf-8");
			requestuid.setHeader("Accept", "application/json");
			StringEntity entityuid = new StringEntity(
					"{\"UserInfoReq\":{\"uid\": \"" + content.substring(begin + 9, end) + "\"} }", "utf-8");// 解决中文乱码问题
			entityuid.setContentEncoding("UTF-8");
			entityuid.setContentType("application/json");
			requestuid.setEntity(entityuid);
			HttpResponse responseuid = clientuid.execute(requestuid);
			BufferedReader inuid = new BufferedReader(new InputStreamReader(responseuid.getEntity().getContent()));
			StringBuffer sbuid = new StringBuffer("");
			String lineuid = "";
			String NLuid = System.getProperty("line.separator");
			while ((lineuid = inuid.readLine()) != null) {
				sbuid.append(lineuid + NLuid);
			}
			inuid.close();
//			System.out.println("USER INFO:" + sbuid.toString());
		} else {
			System.out.println("Error statuscode:" + response.getStatusLine().getStatusCode());
		}
		return resb.toString();
    }
	
}
