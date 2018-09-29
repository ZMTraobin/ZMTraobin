package com.cmig.future.csportal.module.search;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SearchApiInvoking {

	private static Logger logger = LoggerFactory.getLogger(SearchApiInvoking.class);

	private static String it_url;
	private static String yl_url;
	private static String ad_url;
	private  static String it_datasource;
	private static String parameter;
	private static String pageSize;
	private static String delete_url;
    private static String news_url;
    private static String store_datasource;
    private static String store_url;
	/**
	 * 获取配置文件数据
	 */
	static {
		InputStream inputStream = SearchApiInvoking.class.getClassLoader().getResourceAsStream("search.properties");
		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			it_url=properties.getProperty("it_url");
			yl_url=properties.getProperty("yl_url");
			ad_url=properties.getProperty("ad_url");
			delete_url=properties.getProperty("delete_url");
			news_url=properties.getProperty("news_url");
			it_datasource=properties.getProperty("it_datasource");
			parameter=properties.getProperty("parameter");
			pageSize=properties.getProperty("pageSize");
			store_datasource=properties.getProperty("store_datasource");
			store_url=properties.getProperty("store_url");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	/**
	 * @author hebolong
	 * @Description: post接口调用
	 * @param url 访问路径
	 * @param stringJson 传输数据
	 */
	public static void apiInvoking(String url,String stringJson)
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		StringEntity stringEntity = new StringEntity(stringJson,"utf-8");
		stringEntity.setContentEncoding("utf-8");
		stringEntity.setContentType("application/json");
		httpPost.setEntity(stringEntity);
		BufferedReader reader = null;
		String message=null;
		try {
			HttpResponse response=httpClient.execute(httpPost);
			response.setHeader("Content-Type","charset=UTF-8");			
			int state=response.getStatusLine().getStatusCode();
			InputStream inputStream=response.getEntity().getContent();			
			reader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
			message=reader.readLine();
			logger.info("调用添加数据接口状态："+state);
			logger.info("接口返回信息:"+message);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author hebolong
	 * @Description:访问接口获取数据
	 * @return
	 */
	public static String getData(String tag)
	{    
		String responseMsg="";
		HttpClient httpClient = new HttpClient();
		StringBuffer url=new StringBuffer("");
		if("".equals(tag))
		{
			url.append(it_datasource).append(pageSize).append(parameter);
			logger.info("调用爱投数据接口");
		}else
		{
			url.append(store_datasource);
			logger.info("调用商户数据接口");
		}
		GetMethod getMethod = new GetMethod(url.toString());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		try {
			httpClient.executeMethod(getMethod);
			int state=getMethod.getStatusLine().getStatusCode();
			logger.info("获取数据接口调用状态:"+state);
			byte[] responseBody = getMethod.getResponseBody();
			responseMsg = new String(responseBody);	
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			getMethod.releaseConnection();
		}
		return responseMsg;
	}


	public static String getIt_url()
	{
		return it_url;
	}

	public static String getYl_url()
	{
		return yl_url;
	}

	public static String getAd_url()
	{
		return ad_url;
	}

	public static String getIt_datasource()
	{
		return it_datasource;
	}

	public static String getParameter()
	{
		return parameter;
	}

	public static String getPageSize()
	{
		return pageSize;
	}

	public static String getDelete_Url()
	{
		return delete_url;
	}
	
	public static String getNewsUrl()
	{
		return news_url;
	}
	 
	public static String getStoreData()
	{
		return store_datasource;
	}
	
	public static String getStoreUrl()
	{
		return store_url;
	}
	
}

